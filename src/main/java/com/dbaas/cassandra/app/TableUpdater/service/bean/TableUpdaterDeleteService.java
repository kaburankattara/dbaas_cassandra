package com.dbaas.cassandra.app.TableUpdater.service.bean;

import com.dbaas.cassandra.domain.cassandra.table.service.TableService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableUpdaterDeleteService {

	private ServerService serverService;

	private TableService tableService;

	@Autowired
	TableUpdaterDeleteService(ServerService serverService, TableService tableService) {
		this.serverService = serverService;
		this.tableService = tableService;
	}

	/**
	 * テーブルを削除する
	 */
	public void deleteTable(LoginUser user, String keyspace, String tableName) {
		try {
			Instances instances = serverService.getInstances(user);
			for (Instance instance : instances.getInstanceList()) {
				tableService.deleteTable(instance, keyspace, tableName);
				// TODO マルチノード対応したときに複数インスタンスを考慮した修正を行う
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new RuntimeException();
		}
	}
}
