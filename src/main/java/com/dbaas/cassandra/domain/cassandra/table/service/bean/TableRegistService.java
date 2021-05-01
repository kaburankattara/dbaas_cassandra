package com.dbaas.cassandra.domain.cassandra.table.service.bean;

import com.dbaas.cassandra.domain.cassandra.Cassandra;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.server.instance.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableRegistService {

	@Autowired
	public TableRegistService() {
	}

	public void registTable(Instance instance, String keyspace, Table table) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// create文を作成して実行
		String cqlCommand = table.getCreateCql(keyspace);
		cassandraServer.execCql(instance, cqlCommand);
	}
}
