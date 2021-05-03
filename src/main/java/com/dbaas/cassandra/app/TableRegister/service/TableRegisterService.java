package com.dbaas.cassandra.app.TableRegister.service;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.cassandra.table.dto.RegistTableResultDto;
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
public class TableRegisterService {

	private ServerService serverService;

	private TableService tableService;

	@Autowired
	TableRegisterService(ServerService serverService, TableService tableService) {
		this.serverService = serverService;
		this.tableService = tableService;
	}

	/**
	 * テーブルを登録する
	 */
	public RegistTableResultDto registTable(LoginUser user, Keyspace keyspace, Table table) {

		// 入力チェックしエラーの場合、処理を中断する
		RegistTableResultDto validateResult = tableService.validateForRegist(user, keyspace, table);
		if (validateResult.hasError()) {
			return validateResult;
		}

		// 入力されたテーブルを登録する
		Instances instances = serverService.getInstances(user);
		for (Instance instance : instances.getInstanceList()) {
			tableService.registTable(instance, keyspace, table);
		}

		return validateResult;
	}

	/**
	 * テーブルを取得する
	 */
	public Table findTableByRetry(LoginUser user, Keyspace keyspace, Table table) {
		// 登録済みのテーブルを取得する
		Instances instances = serverService.getInstances(user);

		// TODO 後で正規化
		int tryCount = 1;
		int MAX_TRY_COUNT = 3;
		Table findedTable = new Table();
		boolean isRetry = true;
		while (isRetry) {
			// テーブルが取得出来るか、最大リトライ回数まで検索処理を実施
			findedTable = tableService.findTableByKeyspace(instances, keyspace, table.getTableName());
			if (findedTable.isEmpty() && tryCount <= MAX_TRY_COUNT) {
				tryCount++;
				continue;
			}
			isRetry = false;
		}

		return findedTable;
	}
}
