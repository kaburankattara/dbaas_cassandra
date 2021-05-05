package com.dbaas.cassandra.domain.cassandra.keyspace.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.cassandra.Cassandra;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.server.instance.Instance;

@Service
@Transactional
public class KeyspaceDeleteService {

    @Autowired
	public KeyspaceDeleteService() {
	}

	public void deleteKeyspace(Instance instance, Keyspace keyspace) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// 実行文を生成
		String cqlCommand = "drop keyspace " + keyspace.getKeyspace() + ";";

		// 実行
		cassandraServer.execCql(instance, cqlCommand);
	}
}
