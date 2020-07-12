package com.dbaas.cassandra.app.keySpaceList.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;

@Service
@Transactional
public class  KeySpaceListInitService {
	
	private ServerManagerService serverManagerService;
	
	private CassandraManagerService cassandraManagerService;
	
	@Autowired
	KeySpaceListInitService(ServerManagerService serverManagerService,
			CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}
	
	/**
	 * cassandraの起動状況をリフレッシュする
	 * 
	 * @param user
	 */
	public void refreshCassandra(LoginUser user) {
		try {
			Instances instances = serverManagerService.getInstances(user);
			boolean canAllExecCql = cassandraManagerService.canAllExecCql(instances);

			// サーバはあるが、cassandraが起動来ていない場合
			// cassandraを再セットアップし、起動
			if (!instances.isEmpty() && canAllExecCql) {
				cassandraManagerService.setup(user, instances);
				cassandraManagerService.execCassandraByWait(instances);
				return;
			}

			// サーバが無ければ構築
			// EC2を構築
			// TODO マルチノードにしたときにメソッドを統合する
			serverManagerService.createServer(user);
			// 保持しているサーバの状態が全てrunningになるまで待つ
			serverManagerService.waitCompleteCreateServer(user);

			// cassandraをセットアップし、cassandraを起動
			cassandraManagerService.setup(user, instances);
			cassandraManagerService.execCassandraByWait(instances);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}
}
