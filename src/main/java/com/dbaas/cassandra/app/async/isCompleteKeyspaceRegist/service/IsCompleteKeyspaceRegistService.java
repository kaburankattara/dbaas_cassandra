package com.dbaas.cassandra.app.async.isCompleteKeyspaceRegist.service;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.KeyspaceService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IsCompleteKeyspaceRegistService {

	private ServerService serverService;

	private KeyspaceService keyspaceService;

	@Autowired
	IsCompleteKeyspaceRegistService(ServerService serverService, KeyspaceService keyspaceService) {
		this.serverService = serverService;
		this.keyspaceService = keyspaceService;
	}

	/**
	 * サーバの作成が完了し、キースペースが登録済みか判定
	 */
	public Keyspaces isCompleteKeyspaceRegist(LoginUser user) {
		Instances instances = serverService.getInstances(user);
		
		// サーバが起動中なら空リストを返す
		if (instances.hasPendingInstance()) {
			return Keyspaces.createEmptyInstance();
		}
		
		// サーバが起動済みであればキースペースを取得
		Keyspaces keyspaces = keyspaceService.findAllKeyspaceWithoutSysKeyspace(instances);

		// 取得したキースペースが空で無い場合、紐づくキースペース登録予定を削除する
		if (!keyspaces.isEmpty()) {
			keyspaceService.deleteKeyspaceRegistPlan(user, keyspaces);
		}

		return keyspaces;
	}
}
