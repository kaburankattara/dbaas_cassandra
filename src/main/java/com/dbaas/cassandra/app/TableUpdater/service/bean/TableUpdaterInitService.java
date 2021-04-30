package com.dbaas.cassandra.app.TableUpdater.service.bean;

import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableUpdaterInitService {

	private ServerService serverService;

	private CassandraService cassandraService;

	@Autowired
	TableUpdaterInitService(ServerService serverService, CassandraService cassandraService) {
		this.serverService = serverService;
		this.cassandraService = cassandraService;
	}

	/**
	 * テーブルを取得する
	 */
	public Table findTableByKeyspaceAndTableName(LoginUser user, String keyspace, String tableName) {
		try {
			Instances instances = serverService.getInstances(user);
			for (Instance instance : instances.getInstanceList()) {
				return cassandraService.findTableByKeyspace(instance, keyspace, tableName);
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
