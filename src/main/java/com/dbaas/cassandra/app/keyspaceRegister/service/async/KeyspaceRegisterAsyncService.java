package com.dbaas.cassandra.app.keyspaceRegister.service.async;

import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.KeyspaceService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.setSysDate;

@Service
@Transactional
public class  KeyspaceRegisterAsyncService {
	
	private ServerService serverService;

	private KeyspaceService keyspaceService;
	
	private CassandraService cassandraService;

	@Autowired
	KeyspaceRegisterAsyncService(ServerService serverService, KeyspaceService keyspaceService
			, CassandraService cassandraService) {
		this.serverService = serverService;
		this.keyspaceService = keyspaceService;
		this.cassandraService = cassandraService;
	}
	
	/**
	 * キースペースを登録する
	 */
	@Async()
	public void registKeyspace(LoginUser user, Keyspace keyspace, LocalDateTime sysDate) {
		setSysDate(sysDate);
		
		try {
			Instances instances = serverService.getInstances(user);
			boolean canAllExecCql = cassandraService.canAllExecCql(instances);

			// CQLが実行可能の場合、キースペースの登録のみ実施
			if (canAllExecCql) {
				// ※他画面からの登録処理とバッティングを考慮して登録する
				keyspaceService.registKeyspaceByDuplicateIgnore(instances, keyspace);
				return;
			}
			
			// サーバはあるが、cassandraが起動来ていない場合
			// cassandraを再セットアップし、起動してからキースペースを登録
			if (!instances.isEmpty() && !canAllExecCql) {
				cassandraService.setup(user, instances);
				cassandraService.execCassandraByWait(instances);
				// ※他画面からの登録処理とバッティングを考慮して登録する
				keyspaceService.registKeyspaceByDuplicateIgnore(instances, keyspace);
				return;
			}

			// サーバが無ければ構築し、キースペースを登録する
			// EC2を構築
			// TODO マルチノードにしたときにメソッドを統合する
			serverService.createServer(user);
			// 保持しているサーバの状態が全てrunningになるまで待つ
			serverService.waitCompleteCreateServer(user);
							
			// cassandraをセットアップし、cassandraを起動
			cassandraService.setup(user, instances);
			cassandraService.execCassandraByWait(instances);
			
			// 入力されたkeyspaceを登録する
			// ※他画面からの登録処理とバッティングを考慮して登録する
			keyspaceService.registKeyspaceByDuplicateIgnore(instances, keyspace);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}
}
