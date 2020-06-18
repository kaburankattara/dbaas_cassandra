package com.dbaas.cassandra.app.TableUpdater.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;

@Service
@Transactional
public class TableUpdaterInitService {

	private ServerManagerService serverManagerService;

	private CassandraManagerService cassandraManagerService;

	@Autowired
	TableUpdaterInitService(ServerManagerService serverManagerService, CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}

	/**
	 * テーブルを登録する
	 */
	public void registTable(LoginUser user, String keySpace, Table table) {
		try {
			// 入力されたテーブルを登録する
			Instances instances = serverManagerService.getInstances(user);
			for (Instance instance : instances.getInstanceList()) {
				cassandraManagerService.registTable(instance, keySpace, table);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}
}
