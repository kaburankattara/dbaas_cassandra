package com.dbaas.cassandra.app.TableRegister.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class TableRegisterService {

	private ServerService serverService;

	private CassandraService cassandraService;

	@Autowired
	TableRegisterService(ServerService serverService, CassandraService cassandraService) {
		this.serverService = serverService;
		this.cassandraService = cassandraService;
	}

	/**
	 * テーブルを登録する
	 */
	public void registTable(LoginUser user, String keyspace, Table table) {
		// 入力されたテーブルを登録する
		Instances instances = serverService.getInstances(user);
		for (Instance instance : instances.getInstanceList()) {
			cassandraService.registTable(instance, keyspace, table);
		}
	}

	/**
	 * テーブルを取得する
	 */
	public Table findTableByRetry(LoginUser user, String keyspace, Table table) {
		//  登録済みのテーブルを取得する
		Instances instances = serverService.getInstances(user);

		int tryCount = 1;
		int MAX_TRY_COUNT = 3;
		Table findedTable = new Table();
		boolean isRetry = true;
		while (isRetry) {
			// テーブルが取得出来るか、最大リトライ回数まで検索処理を実施
			findedTable = cassandraService.findTableByKeyspace(instances, keyspace, table.getTableName());
			if (findedTable.isEmpty() && tryCount <= MAX_TRY_COUNT) {
				tryCount++;
				continue;
			}
			isRetry = false;
		}

		return findedTable;
	}
}
