package com.dbaas.cassandra.app.TableUpdater.service.bean;

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
public class TableUpdaterUpdateService {

	private ServerService serverService;

	private TableService tableService;

	@Autowired
	TableUpdaterUpdateService(ServerService serverService, TableService tableService) {
		this.serverService = serverService;
		this.tableService = tableService;
	}

	/**
	 * テーブルを更新する
	 */
	public RegistTableResultDto updateTable(LoginUser user, Keyspace keyspace, Table table) {
		try {

			// 入力チェックしエラーの場合、処理を中断する
			RegistTableResultDto validateResult = tableService.validateForUpdate(user, keyspace, table);
			if (validateResult.hasError()) {
				return validateResult;
			}

			Instances instances = serverService.getInstances(user);
			for (Instance instance : instances.getInstanceList()) {
				tableService.addColumns(instance, keyspace, table);
				// TODO マルチノード対応したときに複数インスタンスを考慮した修正を行う
			}

			return validateResult;

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new RuntimeException();
		}
	}
}
