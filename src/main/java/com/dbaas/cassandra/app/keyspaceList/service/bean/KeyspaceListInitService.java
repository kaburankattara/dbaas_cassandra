package com.dbaas.cassandra.app.keyspaceList.service.bean;

import java.util.ArrayList;
import java.util.List;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.KeyspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class KeyspaceListInitService {
	
	private ServerService serverService;
	
	private CassandraService cassandraService;

	private KeyspaceService keyspaceService;
	
	@Autowired
	KeyspaceListInitService(ServerService serverService,
							CassandraService cassandraService, KeyspaceService keyspaceService) {
		this.serverService = serverService;
		this.cassandraService = cassandraService;
		this.keyspaceService = keyspaceService;
	}
	
	/**
	 * 現在、作成済みを確認出来たキースペースのリストを取得する
	 * 
	 * @param user
	 * @return
	 */
	public Keyspaces findCreatedKeyspaceList(LoginUser user) {
			Instances instances = serverService.getInstances(user);
			
			// サーバが起動中なら空のキースペースリストを返す
			if (instances.hasPendingInstance()) {
				return Keyspaces.createEmptyInstance();
			}
			
			// 登録済みのキースペースリストを取得する
			return keyspaceService.findAllKeyspaceWithoutSysKeyspaceNoRetry(instances);
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

			// サーバが起動中なら完了するまで待つ
			Instances instances = serverService.getInstances(user);
			if (instances.hasPendingInstance()) {
				serverService.waitCompleteCreateServer(user);
			}

			// cassandraのコマンド実行まで可能な場合、
			// キースペースの登録漏れがあれば登録しておく
			instances = serverService.getInstances(user);
			boolean canAllExecCql = cassandraService.canAllExecCql(instances);
			if (!instances.isEmpty() && canAllExecCql) {
				keyspaceService.registKeyspaceByDuplicateIgnore(instances, keyspaceRegistPlans);
				return;
			}
			
			// サーバはあるが、cassandraが起動来ていない場合
			// cassandraを再セットアップし、起動
			// そしてキースペースの登録漏れがあれば登録
			if (!instances.isEmpty() && !canAllExecCql) {
				cassandraService.setup(user, instances);
				cassandraService.execCassandraByWait(instances);
				keyspaceService.registKeyspaceByDuplicateIgnore(instances, keyspaceRegistPlans);
				return;
			}

			// サーバが無ければ構築し、登録漏れのキースペースを登録
			// EC2を構築
			// TODO マルチノードにしたときにメソッドを統合する
			serverService.createServer(user);
			// 保持しているサーバの状態が全てrunningになるまで待ち、
			// インスタンスを再取得する
			serverService.waitCompleteCreateServer(user);
			instances = serverService.getInstances(user);
			
			// cassandraをセットアップし、cassandraを起動
			cassandraService.setup(user, instances);
			cassandraService.execCassandraByWait(instances);
			// そしてキースペースの登録漏れがあれば登録
			keyspaceService.registKeyspaceByDuplicateIgnore(instances, keyspaceRegistPlans);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new Exception();
		}
	}
	
	
}
