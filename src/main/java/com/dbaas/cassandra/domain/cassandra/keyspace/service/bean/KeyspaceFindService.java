package com.dbaas.cassandra.domain.cassandra.keyspace.service.bean;

import com.dbaas.cassandra.domain.cassandra.Cassandra;
import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlan;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.cassandra.keyspace.dto.RegistKeyspaceResultDto;
import com.dbaas.cassandra.domain.message.MessageSourceService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanDao;
import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanEntity;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.shared.validation.ValidateResult;
import com.dbaas.cassandra.shared.validation.Validator;
import com.dbaas.cassandra.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.SmartValidator;

import java.util.ArrayList;
import java.util.List;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.*;
import static com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;
import static com.dbaas.cassandra.domain.message.Message.MSG005E;
import static com.dbaas.cassandra.utils.StringUtils.replaceAll;
import static com.dbaas.cassandra.utils.StringUtils.split;
import static java.util.Arrays.asList;

@Service
@Transactional
public class KeyspaceFindService {

    @Autowired
	public KeyspaceFindService() {
	}

	public Keyspaces findAllKeyspaceWithoutSysKeyspace(Instances instances) {
		List<Keyspace> keyspaceList = new ArrayList<>();
		// システム管理用キースペース以外を抽出
		for (Instance instance : instances.getInstanceList()) {
			keyspaceList.addAll(findAllKeyspaceWithoutSysKeyspace(instance).getKeyspaceList());
		}
		return Keyspaces.createInstance(keyspaceList);
	}

	public Keyspaces findAllKeyspaceWithoutSysKeyspace(Instance instance) {
		List<Keyspace> keyspaceList = new ArrayList<Keyspace>();
		// システム管理用キースペース以外を抽出
		for (Keyspace keyspace : findAllKeyspace(instance).getKeyspaceList()) {
			if (SYSTEM_KEYSPACE_LIST.contains(keyspace.getKeyspace())) {
				continue;
			}
			keyspaceList.add(keyspace);
		}
		return Keyspaces.createInstance(keyspaceList);
	}

	public Keyspaces findAllKeyspace(Instances instances) {
		// 各サーバの保持しているキースペース一覧を取得
		Keyspaces keyspaces = Keyspaces.createEmptyInstance();
		for (Instance instance : instances.getInstanceList()) {
			keyspaces.addAll(findAllKeyspace(instance));
		}
		return keyspaces;
	}

	public Keyspaces findAllKeyspace(Instance instance) {
		// 各サーバの保持しているキースペース一覧を取得
		List<String> keyspaceList = new ArrayList<>();
		String result = null;

		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// create文を作成して実行
		String cqlCommand = "DESC KEYSPACES";
		result = cassandraServer.execCql(instance, cqlCommand);

		// 検索結果をListに整形
		result = replaceAll(result, "\n", "");
		result = replaceAll(result, "  ", " ");
		List<String> cqlResultKeyspaceList = new ArrayList<String>(asList(split(result, ' ')));

		// システム管理用キースペース以外を抽出
		for (String keyspace : cqlResultKeyspaceList) {
			keyspaceList.add(keyspace);
		}

		return Keyspaces.createInstanceByStringList(keyspaceList);
	}
}
