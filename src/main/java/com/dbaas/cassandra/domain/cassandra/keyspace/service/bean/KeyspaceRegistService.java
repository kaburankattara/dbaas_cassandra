package com.dbaas.cassandra.domain.cassandra.keyspace.service.bean;

import com.dbaas.cassandra.domain.cassandra.Cassandra;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.*;
import static com.dbaas.cassandra.utils.StringUtils.replaceAll;
import static com.dbaas.cassandra.utils.StringUtils.split;
import static java.util.Arrays.asList;

@Service
@Transactional
public class KeyspaceRegistService {

	private KeyspaceFindService keyspaceFindService;

    @Autowired
	public KeyspaceRegistService(KeyspaceFindService keyspaceFindService) {
    	this.keyspaceFindService = keyspaceFindService;
	}

	public void registKeyspace(Instances instances, Keyspace keyspace) {
		for (Instance instance : instances.getInstanceList()) {
			registKeyspace(instance, keyspace);
		}
	}

	/**
	 * 重複は無視してキースペースを登録
	 *
	 * @param instances
	 * @param keyspaceRegistPlans
	 */
	public void registKeyspaceByDuplicatIgnore(Instances instances, KeyspaceRegistPlans keyspaceRegistPlans) {
		for (String keyspace : keyspaceRegistPlans.getKeyspaceList()) {
			registKeyspaceByDuplicatIgnore(instances, Keyspace.createInstance(keyspace));
		}
	}

	public void registKeyspaceByDuplicatIgnore(Instances instances, Keyspace keyspace) {
		for (Instance instance : instances.getInstanceList()) {
			registKeyspaceByDuplicatIgnore(instance, keyspace);
		}
	}

	/**
	 * 重複は無視してキースペースを登録
	 *
	 * @param instance
	 * @param keyspace
	 */
	public void registKeyspaceByDuplicatIgnore(Instance instance, Keyspace keyspace) {
		// 対象インスタンスのキースペース一覧を取得
		Keyspaces keyspaces = keyspaceFindService.findAllKeyspaceWithoutSysKeyspace(instance);

		// キースペースが重複するのであれば登録しない
		if (keyspaces.hasKeyspace(keyspace)) {
			return;
		}

		// キースペースが重複しないのであれば登録
		registKeyspace(instance, keyspace);
	}

	public void registKeyspace(Instance instance, Keyspace keyspace) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// create文を作成して実行
		String cqlCommand = "create keyspace if not exists " + keyspace.getKeyspace()
				+ " with replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };";
		cassandraServer.execCql(instance, cqlCommand);
	}
}
