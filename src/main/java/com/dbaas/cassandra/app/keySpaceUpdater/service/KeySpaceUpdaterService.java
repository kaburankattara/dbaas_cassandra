package com.dbaas.cassandra.app.keySpaceUpdater.service;

import static com.dbaas.cassandra.utils.ThreadUtils.sleep;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;

@Service
@Transactional
public class  KeySpaceUpdaterService {
	
	private ServerManagerService serverManagerService;
	
	private CassandraManagerService cassandraManagerService;
	
	@Autowired
	KeySpaceUpdaterService(ServerManagerService serverManagerService, CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}
	
	/**
	 * テーブル一覧を取得
	 */
	public List<String> findTable(LoginUser user, String keySpace) {
		try {
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
			
			// 入力されたkeySpaceを登録する
			Instances instances = serverManagerService.getInstances(user);
			return cassandraManagerService.findTableByKeySpace(instances, keySpace);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		return null;
	}
}
