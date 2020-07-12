package com.dbaas.cassandra.app.keySpaceRegister.service.async;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.setSysDate;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;

@Service
@Transactional
public class  KeySpaceRegisterAsyncService {
	
	private ServerManagerService serverManagerService;
	
	private CassandraManagerService cassandraManagerService;
	
	@Autowired
	KeySpaceRegisterAsyncService(ServerManagerService serverManagerService,
			CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}
	
	/**
	 * キースペースを登録する
	 */
	@Async()
	public void registKeySpace(LoginUser user, String keySpace, LocalDateTime sysDate) {
		setSysDate(sysDate);
		
		try {
			Instances instances = serverManagerService.getInstances(user);
			boolean canAllExecCql = cassandraManagerService.canAllExecCql(instances);

			// CQLが実行可能の場合、キースペースの登録のみ実施
			if (canAllExecCql) {
				// ※他画面からの登録処理とバッティングを考慮して登録する
				cassandraManagerService.registKeySpaceByDuplicatIgnore(instances, keySpace);
				return;
			}
			
			// サーバはあるが、cassandraが起動来ていない場合
			// cassandraを再セットアップし、起動してからキースペースを登録
			if (!instances.isEmpty() && canAllExecCql) {
				cassandraManagerService.setup(user, instances);
				cassandraManagerService.execCassandraByWait(instances);
				// ※他画面からの登録処理とバッティングを考慮して登録する
				cassandraManagerService.registKeySpaceByDuplicatIgnore(instances, keySpace);
				return;
			}

			// サーバが無ければ構築し、キースペースを登録する
			// EC2を構築
			// TODO マルチノードにしたときにメソッドを統合する
			serverManagerService.createServer(user);
			// 保持しているサーバの状態が全てrunningになるまで待つ
			serverManagerService.waitCompleteCreateServer(user);
							
			// cassandraをセットアップし、cassandraを起動
			cassandraManagerService.setup(user, instances);
			cassandraManagerService.execCassandraByWait(instances);
			
			// 入力されたkeySpaceを登録する
			// ※他画面からの登録処理とバッティングを考慮して登録する
			cassandraManagerService.registKeySpaceByDuplicatIgnore(instances, keySpace);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}
}
