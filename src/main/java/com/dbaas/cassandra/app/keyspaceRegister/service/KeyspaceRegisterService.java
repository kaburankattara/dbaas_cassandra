package com.dbaas.cassandra.app.keyspaceRegister.service;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.getSysDate;

import java.util.List;

import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.keyspaceRegister.service.async.KeyspaceRegisterAsyncService;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlanService;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class  KeyspaceRegisterService {
	
	private KeyspaceRegisterAsyncService asyncService;
	
	private KeyspaceRegistPlanService keyspaceRegistPlanService;

	private ServerService serverService;

	private CassandraService cassandraService;
	
	@Autowired
	KeyspaceRegisterService(KeyspaceRegisterAsyncService asyncService, KeyspaceRegistPlanService keyspaceRegistPlanService, ServerService serverService, CassandraService cassandraService) {
		this.asyncService = asyncService;
		this.keyspaceRegistPlanService = keyspaceRegistPlanService;
		this.serverService = serverService;
		this.cassandraService = cassandraService;
	}
	
	/**
	 * キースペース一覧の取得
	 * 
	 * @param user
	 * @return
	 */
	public List<String> findAllKeyspace(LoginUser user) {
		return keyspaceRegistPlanService.findKeyspaceRegistPlanByUserId(user).getKeyspaceList();
	}
	
	/**
	 * キースペースを登録する
	 *
	 * @param user ユーザー
	 * @param keyspace キースペース
	 * @return 登録の成功判定S
	 */
	public boolean registKeyspace(LoginUser user, String keyspace) {
		// cassandraからキースペース一覧を取得する
		Instances instances = serverService.getAllInstances(user);
		Keyspaces keyspaces = cassandraService.findAllKeyspace(instances);

		// 引数のキースペースが登録済の場合、エラーとして処理を中断する
		if (keyspaces.hasKeyspace(keyspace)) {
			return false;
		}

		// 登録するキースペースをキースペースマネージャーテーブルに登録
		keyspaceRegistPlanService.insert(user, keyspace);
		
		// サーバ構築が必要となる場合があるため、
		// 物理的なキースペースの登録は非同期で行う
		asyncService.registKeyspace(user, keyspace, getSysDate());
		return true;
	}
}
