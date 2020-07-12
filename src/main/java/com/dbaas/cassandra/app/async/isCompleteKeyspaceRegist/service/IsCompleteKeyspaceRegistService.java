package com.dbaas.cassandra.app.async.isCompleteKeyspaceRegist.service;

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
public class IsCompleteKeyspaceRegistService {

	private ServerManagerService serverManagerService;

	private CassandraManagerService cassandraManagerService;

	@Autowired
	IsCompleteKeyspaceRegistService(ServerManagerService serverManagerService, CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}

	/**
	 * サーバの作成が完了し、キースペースが登録済みか判定
	 */
	public List<String> isCompleteKeyspaceRegist(LoginUser user) {
		Instances instances = serverManagerService.getInstances(user);
		
		// サーバが起動中ならfalse
		if (instances.hasPendingInstance()) {
			return new ArrayList<String>();
		}
		
		// サーバが起動済みであればキースペースを一覧取得し、cassandraの起動を確認
		return cassandraManagerService.findAllKeySpaceWithoutSysKeySpace(instances);
	}
}
