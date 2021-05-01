package com.dbaas.cassandra.domain.cassandra.table.service.bean;

import com.dbaas.cassandra.domain.cassandra.Cassandra;
import com.dbaas.cassandra.domain.cassandra.cql.CqlFactory;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.*;

@Service
@Transactional
public class TableAlterService {

	private TableFindService tableFindService;

	private TableRegistService tableRegistService;

	private TableDeleteService tableDeleteService;

	@Autowired
	public TableAlterService(TableFindService tableFindService) {
		this.tableFindService = tableFindService;
	}

	public Tables addColumns(Instance instance, String keyspace, Table newTable) {
		String result = null;

		// 現行のテーブル情報を取得
		Instances instances = Instances.createInstance(instance);
		// TODO  返り値がおかしい？
		Table oldTable = tableFindService.findTableByKeyspace(instances, keyspace, newTable.getTableName());

		// テーブルを現行と次期で比較し、Alter文を生成
		CqlFactory cqlFactory = new CqlFactory();
		List<String> cqlAlterCommandList = cqlFactory.createAlterCqlForAddColumns(keyspace, oldTable, newTable);

		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// 実行文を生成
		StringBuilder sb = new StringBuilder();
		sb.append(CQL_COMMAND_USE + KEYSPACE_SYSTEM_SCHEMA + ";");
		for (String cqlAlterCommand : cqlAlterCommandList) {
			sb.append(cqlAlterCommand);
		}
		String cqlCommand = sb.toString();

		// 実行
		result = cassandraServer.execCql(instance, cqlCommand);
		return new Tables(result);
	}
}
