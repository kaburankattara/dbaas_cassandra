package com.dbaas.cassandra.domain.cassandra.table.service;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.cassandra.table.dto.RegistTableResultDto;
import com.dbaas.cassandra.domain.cassandra.table.service.bean.*;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

	private TableFindService tableFindService;

	private TableRegistService tableRegistService;

	private TableDeleteService tableDeleteService;

	private TableAlterService tableAlterService;

	private TableValidateService tableValidateService;

	@Autowired
	public TableService(TableFindService tableFindService, TableRegistService tableRegistService
			, TableDeleteService tableDeleteService, TableAlterService tableAlterService
			, TableValidateService tableValidateService) {
		this.tableFindService = tableFindService;
		this.tableRegistService = tableRegistService;
		this.tableDeleteService = tableDeleteService;
		this.tableAlterService = tableAlterService;
		this.tableValidateService = tableValidateService;
	}

	public Tables findAllTableByKeyspace(Instances instances, String keyspace) {
		return tableFindService.findAllTableByKeyspace(instances, keyspace);
	}

	public Table findTableByKeyspace(Instances instances, Keyspace keyspace, String tableName) {
		return tableFindService.findTableByKeyspace(instances, keyspace, tableName);
	}

	public Tables findAllTableByKeyspace(Instance instance, String keyspace) {
		return tableFindService.findAllTableByKeyspace(instance, keyspace);
	}

	public Table findTableByKeyspace(Instance instance, Keyspace keyspace, String tableName) {
		return tableFindService.findTableByKeyspace(instance, keyspace, tableName);
	}

	public RegistTableResultDto validateForRegist(LoginUser user, Keyspace keyspace, Table table) {
		return tableValidateService.validateForRegist(user, keyspace, table);
	}

	public void registTable(Instance instance, Keyspace keyspace, Table table) {
		tableRegistService.registTable(instance, keyspace, table);
	}

	public Tables addColumns(Instance instance, Keyspace keyspace, Table newTable) {
		return tableAlterService.addColumns(instance, keyspace, newTable);
	}

	public void deleteTable(Instance instance, String keyspace, String tableName) {
		tableDeleteService.deleteTable(instance, keyspace, tableName);
	}
}
