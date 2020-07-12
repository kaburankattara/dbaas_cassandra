package com.dbaas.cassandra.app.keySpaceList.service.async;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.setSysDate;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;

@Async()
@Service
@Transactional
public class  KeySpaceListAsyncService {
	
	private ServerManagerService serverManagerService;
	
	private CassandraManagerService cassandraManagerService;
	
	@Autowired
	KeySpaceListAsyncService(ServerManagerService serverManagerService,
			CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}
	
	/**
	 * cassandraの稼働状況をリフレッシュする
	 * 
	 * @param user
	 */
	public void refreshCassandra(LoginUser user, List<String> keyspaceList, LocalDateTime sysDate) {
		setSysDate(sysDate);
		
		try {
			Instances instances = serverManagerService.getInstances(user);
			boolean canAllExecCql = cassandraManagerService.canAllExecCql(instances);

			// cassandraのコマンド実行まで可能な場合、
			// キースペースの登録漏れがあれば登録しておく
			if (!instances.isEmpty() && canAllExecCql) {
				cassandraManagerService.registKeySpaceByDuplicatIgnore(instances, keyspaceList);
				return;
			}

			// サーバはあるが、cassandraが起動来ていない場合
			// cassandraを再セットアップし、起動
			// そしてキースペースの登録漏れがあれば登録
			if (!instances.isEmpty() && !canAllExecCql) {
				cassandraManagerService.setup(user, instances);
				cassandraManagerService.execCassandraByWait(instances);
				cassandraManagerService.registKeySpaceByDuplicatIgnore(instances, keyspaceList);
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
			cassandraManagerService.registKeySpaceByDuplicatIgnore(instances, keyspaceList);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}
}
