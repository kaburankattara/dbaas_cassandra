package com.dbaas.cassandra.domain.cassandra.table.service.bean;

import com.dbaas.cassandra.domain.cassandra.Cassandra;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.*;
import static com.dbaas.cassandra.domain.cassandra.table.Table.createEmptyTable;
import static com.dbaas.cassandra.utils.StringUtils.isNotEmpty;

@Service
@Transactional
public class TableFindService {

	@Autowired
	public TableFindService() {
	}

	public Tables findAllTableByKeyspace(Instances instances, String keyspace) {
		// 各サーバの保持しているテーブル一覧を取得
		for (Instance instance : instances.getInstanceList()) {
			Tables tables = findAllTableByKeyspace(instance, keyspace);
			if (!tables.isEmpty()) {
				return tables;
			}
		}
		return new Tables();

		// 各サーバで取得したテーブルの重複を削除
		// TODO マルチノードで
	}

	public Table findTableByKeyspace(Instances instances, String keyspace, String tableName) {
		// 各サーバの保持しているテーブル一覧を取得
		for (Instance instance : instances.getInstanceList()) {
			Table table = findTableByKeyspace(instance, keyspace, tableName);
			if (!table.isEmpty()) {
				return table;
			}
		}
		return createEmptyTable();

		// 各サーバで取得したテーブルの重複を削除
		// TODO マルチノードで
	}

	public Tables findAllTableByKeyspace(Instance instance, String keyspace) {
		String result = null;

		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// create文を作成
		String cqlCommand = CQL_COMMAND_USE + KEYSPACE_SYSTEM_SCHEMA + "; ";
		cqlCommand = cqlCommand + "select * from " + TABLE_COLUMNS + " where keyspace_name = '" + keyspace + "'";
		cqlCommand = cqlCommand + ";";

		// 実行
		result = cassandraServer.execCql(instance, cqlCommand);
		return new Tables(result);
	}

	public Table findTableByKeyspace(Instance instance, String keyspace, String tableName) {
		String result = null;

		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// create文を作成
		String cqlCommand = CQL_COMMAND_USE + KEYSPACE_SYSTEM_SCHEMA + "; ";
		cqlCommand = cqlCommand + "select * from " + TABLE_COLUMNS + " where keyspace_name = '" + keyspace + "'";
		if (isNotEmpty(tableName)) {
			cqlCommand = cqlCommand + " and table_name = '" + tableName + "'";
		}
		cqlCommand = cqlCommand + ";";

		// 実行
		result = cassandraServer.execCql(instance, cqlCommand);
		return new Tables(result).getFirstTable();
	}
}
