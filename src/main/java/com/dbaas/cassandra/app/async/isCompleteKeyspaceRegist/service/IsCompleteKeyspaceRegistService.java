package com.dbaas.cassandra.app.async.isCompleteKeyspaceRegist.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class IsCompleteKeyspaceRegistService {

	private ServerService serverService;

	private CassandraService cassandraService;

	@Autowired
	IsCompleteKeyspaceRegistService(ServerService serverService, CassandraService cassandraService) {
		this.serverService = serverService;
		this.cassandraService = cassandraService;
	}

	/**
	 * サーバの作成が完了し、キースペースが登録済みか判定
	 */
	public List<String> isCompleteKeyspaceRegist(LoginUser user) {
		Instances instances = serverService.getInstances(user);
		
		// サーバが起動中ならfalse
		if (instances.hasPendingInstance()) {
			return new ArrayList<String>();
		}
		
		// サーバが起動済みであればキースペースを一覧取得し、cassandraの起動を確認
		return cassandraService.findAllKeySpaceWithoutSysKeySpace(instances);
	}
}
