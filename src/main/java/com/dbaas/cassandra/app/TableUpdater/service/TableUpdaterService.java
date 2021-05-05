package com.dbaas.cassandra.app.TableUpdater.service;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.table.dto.RegistTableResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.TableUpdater.service.bean.TableUpdaterDeleteService;
import com.dbaas.cassandra.app.TableUpdater.service.bean.TableUpdaterInitService;
import com.dbaas.cassandra.app.TableUpdater.service.bean.TableUpdaterUpdateService;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class TableUpdaterService {
	
	private TableUpdaterInitService initService;
	
	private TableUpdaterUpdateService updateService;
	
	private TableUpdaterDeleteService deleteService;

	@Autowired
	TableUpdaterService(TableUpdaterInitService initService, TableUpdaterUpdateService updateService,
			TableUpdaterDeleteService deleteService) {
		this.initService = initService;
		this.updateService = updateService;
		this.deleteService = deleteService;
	}

	/**
	 * テーブルを検索する
	 */
	public Table findTable(LoginUser user, Keyspace keyspace, String tableName) {
		return initService.findTableByKeyspaceAndTableName(user, keyspace, tableName);
	}

	/**
	 * テーブルを更新する
	 */
	public RegistTableResultDto updateTable(LoginUser user, Keyspace keyspace, Table table) {
		return updateService.updateTable(user, keyspace, table);
	}

	/**
	 * テーブルを削除する
	 */
	public void deleteTable(LoginUser user, String keyspace, String tableName) {
		deleteService.deleteTable(user, keyspace, tableName);
	}
}
