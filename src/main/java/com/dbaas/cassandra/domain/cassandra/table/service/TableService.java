package com.dbaas.cassandra.domain.cassandra.table.service;

import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.cassandra.table.service.bean.TableAlterService;
import com.dbaas.cassandra.domain.cassandra.table.service.bean.TableDeleteService;
import com.dbaas.cassandra.domain.cassandra.table.service.bean.TableFindService;
import com.dbaas.cassandra.domain.cassandra.table.service.bean.TableRegistService;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
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

	@Autowired
	public TableService(TableFindService tableFindService, TableRegistService tableRegistService
			, TableDeleteService tableDeleteService, TableAlterService tableAlterService) {
		this.tableFindService = tableFindService;
		this.tableRegistService = tableRegistService;
		this.tableDeleteService = tableDeleteService;
		this.tableAlterService = tableAlterService;
	}

	public void registTable(Instance instance, String keyspace, Table table) {
		tableRegistService.registTable(instance, keyspace, table);
	}

	public Tables findAllTableByKeyspace(Instances instances, String keyspace) {
		return tableFindService.findAllTableByKeyspace(instances, keyspace);
	}

	public Table findTableByKeyspace(Instances instances, String keyspace, String tableName) {
		return tableFindService.findTableByKeyspace(instances, keyspace, tableName);
	}

	public Tables findAllTableByKeyspace(Instance instance, String keyspace) {
		return tableFindService.findAllTableByKeyspace(instance, keyspace);
	}

	public Table findTableByKeyspace(Instance instance, String keyspace, String tableName) {
		return tableFindService.findTableByKeyspace(instance, keyspace, tableName);
	}

	public Tables addColumns(Instance instance, String keyspace, Table newTable) {
		return tableAlterService.addColumns(instance, keyspace, newTable);
	}

	public void deleteTable(Instance instance, String keyspace, String tableName) {
		tableDeleteService.deleteTable(instance, keyspace, tableName);
	}
}
