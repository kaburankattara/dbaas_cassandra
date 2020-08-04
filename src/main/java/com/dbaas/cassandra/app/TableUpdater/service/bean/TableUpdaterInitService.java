package com.dbaas.cassandra.app.TableUpdater.service.bean;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	 * テーブルを取得する
	 */
	public Table findTableByKeySpaceAndTableName(LoginUser user, String keySpace, String tableName) {
		try {
			Instances instances = serverManagerService.getInstances(user);
			for (Instance instance : instances.getInstanceList()) {
				return cassandraManagerService.findTableByKeySpace(instance, keySpace, tableName);
				// TODO マルチノード対応したときに複数インスタンスを考慮した修正を行う
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new RuntimeException();
		}
		return null;
	}
}
