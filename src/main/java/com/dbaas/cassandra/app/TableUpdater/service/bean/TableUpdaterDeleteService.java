package com.dbaas.cassandra.app.TableUpdater.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class TableUpdaterDeleteService {

	private ServerManagerService serverManagerService;

	private CassandraManagerService cassandraManagerService;

	@Autowired
	TableUpdaterDeleteService(ServerManagerService serverManagerService, CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}

	/**
	 * テーブルを削除する
	 */
	public void deleteTable(LoginUser user, String keySpace, String tableName) {
		try {
			Instances instances = serverManagerService.getInstances(user);
			for (Instance instance : instances.getInstanceList()) {
				cassandraManagerService.deleteTable(instance, keySpace, tableName);
				// TODO マルチノード対応したときに複数インスタンスを考慮した修正を行う
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new RuntimeException();
		}
	}
}