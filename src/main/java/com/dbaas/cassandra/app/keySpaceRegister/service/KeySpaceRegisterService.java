package com.dbaas.cassandra.app.keySpaceRegister.service;

import static com.dbaas.cassandra.utils.ThreadUtils.sleep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;

@Service
@Transactional
public class  KeySpaceRegisterService {
	
	private ServerManagerService serverManagerService;
	
	private CassandraManagerService cassandraManagerService;
	
	@Autowired
	KeySpaceRegisterService(ServerManagerService serverManagerService, CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}
	
	/**
	 * キースペースを登録する
	 */
	public void registKeySpace(LoginUser user, String keySpace) {
		boolean isCreateServer = !serverManagerService.hasServer(user);
		try {
			// ユーザがサーバを構築済みであるか判定
			if (isCreateServer) {
				// 未構築なら構築する
				// EC2を構築
				serverManagerService.createServer(user);
				
				// 保持しているサーバの状態が全てrunningになるまで待つ
				boolean isNotAllInstanceRunning = true;
				while (isNotAllInstanceRunning) {
					// ユーザに紐づくEC2リストを取得
					Instances instances = serverManagerService.getInstances(user);
					
					// 保持しているサーバにpendingが存在すればrunningになるまで待つ
					if (instances.hasPendingInstance()) {
						sleep();
						continue;
					}
					
					// 保持しているサーバにpendingが無くなれば次処理を開始する
					isNotAllInstanceRunning = false;
				}
				
				// cassandraをセットアップする
				Instances instances = serverManagerService.getInstances(user);
				for (Instance instance : instances.getInstanceList()) {
					cassandraManagerService.setup(user, instance);
				}
				
				// 保持しているサーバでcassandraが起動していないサーバーがあれば再実行
				while (!cassandraManagerService.isExecAllCassandra(instances)) {
					for (Instance instance : instances.getInstanceList()) {
						cassandraManagerService.execCassandra(instance);
					}
				}
			}				
			
			// 入力されたkeySpaceを登録する
			Instances instances = serverManagerService.getInstances(user);
			for (Instance instance : instances.getInstanceList()) {
				cassandraManagerService.registKeySpace(instance, keySpace);
			}
		} catch (Exception e) {
			// サーバ構築を行っているのであれば、全サーバを削除
			if (isCreateServer) {
				serverManagerService.deleteAllServer(user);
			}
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}
}
