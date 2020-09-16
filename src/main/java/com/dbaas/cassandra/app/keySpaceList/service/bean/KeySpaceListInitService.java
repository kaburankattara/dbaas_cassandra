package com.dbaas.cassandra.app.keySpaceList.service.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class KeySpaceListInitService {
	
	private ServerManagerService serverManagerService;
	
	private CassandraManagerService cassandraManagerService;
	
	@Autowired
	KeySpaceListInitService(ServerManagerService serverManagerService,
			CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}
	
	/**
	 * 現在、作成済みを確認出来たキースペースのリストを取得する
	 * 
	 * @param user
	 * @return
	 */
	public List<String> findCreatedKeyspaceList(LoginUser user) {
			Instances instances = serverManagerService.getInstances(user);
			
			// サーバが起動中なら空のキースペースリストを返す
			if (instances.hasPendingInstance()) {
				return new ArrayList<String>();
			}
			
			// 登録済みのキースペースリストを取得する
			return cassandraManagerService.findAllKeySpaceWithoutSysKeySpace(instances);
	}

	
	/**
	 * cassandraの稼働状況をリフレッシュする
	 * 
	 * @param user
	 * @param keyspaceRegistPlans
	 * @throws Exception
	 */
	public void refreshCassandra(LoginUser user, KeyspaceRegistPlans keyspaceRegistPlans) throws Exception {
		try {
			Instances instances = serverManagerService.getInstances(user);
			boolean canAllExecCql = cassandraManagerService.canAllExecCql(instances);
			
			// サーバが起動中なら完了するまで待つ
			if (instances.hasPendingInstance()) {
				serverManagerService.waitCompleteCreateServer(user);
			}

			// cassandraのコマンド実行まで可能な場合、
			// キースペースの登録漏れがあれば登録しておく
			if (!instances.isEmpty() && canAllExecCql) {
				cassandraManagerService.registKeySpaceByDuplicatIgnore(instances, keyspaceRegistPlans);
				return;
			}
			
			// サーバはあるが、cassandraが起動来ていない場合
			// cassandraを再セットアップし、起動
			// そしてキースペースの登録漏れがあれば登録
			if (!instances.isEmpty() && !canAllExecCql) {
				cassandraManagerService.setup(user, instances);
				cassandraManagerService.execCassandraByWait(instances);
				cassandraManagerService.registKeySpaceByDuplicatIgnore(instances, keyspaceRegistPlans);
				return;
			}

			// サーバが無ければ構築し、登録漏れのキースペースを登録
			// EC2を構築
			// TODO マルチノードにしたときにメソッドを統合する
			serverManagerService.createServer(user);
			// 保持しているサーバの状態が全てrunningになるまで待ち、
			// インスタンスを再取得する
			serverManagerService.waitCompleteCreateServer(user);
			instances = serverManagerService.getInstances(user);
			
			// cassandraをセットアップし、cassandraを起動
			cassandraManagerService.setup(user, instances);
			cassandraManagerService.execCassandraByWait(instances);
			// そしてキースペースの登録漏れがあれば登録
			cassandraManagerService.registKeySpaceByDuplicatIgnore(instances, keyspaceRegistPlans);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new Exception();
		}
	}
	
	
}
