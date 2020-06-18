package com.dbaas.cassandra.app.keySpaceList.service;

import static com.dbaas.cassandra.utils.ThreadUtils.sleep;

import java.util.ArrayList;
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
public class  KeySpaceListService {
	
	private ServerManagerService serverManagerService;
	
	private CassandraManagerService cassandraManagerService;
	
	@Autowired
	KeySpaceListService(ServerManagerService serverManagerService, CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}
	
	/**
	 * キースペースを登録する
	 */
	public List<String> findKeySpace(LoginUser user) {
		boolean hasNotServer = !serverManagerService.hasServer(user);
		try {
			// サーバを保持していないならキースペースは保持していないため、処理終了
			if (hasNotServer) {
				return new ArrayList<String>();
			}
						
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
							
			// TODO cassandraの起動してない場合、起動処理を入れる
			
			// keySpaceの一覧を取得
			Instances instances = serverManagerService.getInstances(user);
			return cassandraManagerService.findAllKeySpace(instances);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		return new ArrayList<String>();
	}
}
