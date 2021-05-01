package com.dbaas.cassandra.domain.cassandra.table.service.bean;

import com.dbaas.cassandra.domain.cassandra.Cassandra;
import com.dbaas.cassandra.domain.server.instance.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableDeleteService {

	@Autowired
	public TableDeleteService() {
	}

	public void deleteTable(Instance instance, String keyspace, String tableName) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// 実行文を生成
		String cqlCommand = "drop table " + keyspace + "." + tableName + ";";

		// 実行
		cassandraServer.execCql(instance, cqlCommand);
	}
}
