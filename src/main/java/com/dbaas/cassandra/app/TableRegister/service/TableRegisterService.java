package com.dbaas.cassandra.app.TableRegister.service;

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
public class TableRegisterService {

	private ServerManagerService serverManagerService;

	private CassandraManagerService cassandraManagerService;

	@Autowired
	TableRegisterService(ServerManagerService serverManagerService, CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
	}

	/**
	 * テーブルを登録する
	 */
	public void registTable(LoginUser user, String keySpace, Table table) {
		// 入力されたテーブルを登録する
		Instances instances = serverManagerService.getInstances(user);
		for (Instance instance : instances.getInstanceList()) {
			cassandraManagerService.registTable(instance, keySpace, table);
		}
	}

	/**
	 * テーブルを取得する
	 */
	public Table findTableByRetry(LoginUser user, String keySpace, Table table) {
		//  登録済みのテーブルを取得する
		Instances instances = serverManagerService.getInstances(user);

		int tryCount = 1;
		int MAX_TRY_COUNT = 3;
		Table findedTable = new Table();
		boolean isRetry = true;
		while (isRetry) {
			// テーブルが取得出来るか、最大リトライ回数まで検索処理を実施
			findedTable = cassandraManagerService.findTableByKeySpace(instances, keySpace, table.getTableName());
			if (findedTable.isEmpty() && tryCount <= MAX_TRY_COUNT) {
				tryCount++;
				continue;
			}
			isRetry = false;
		}

		return findedTable;
	}
}
