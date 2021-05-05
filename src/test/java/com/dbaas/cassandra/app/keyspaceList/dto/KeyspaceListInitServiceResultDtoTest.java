package com.dbaas.cassandra.app.keyspaceList.dto;

import static com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;

import java.util.ArrayList;
import java.util.List;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlan;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;

public class KeyspaceListInitServiceResultDtoTest {
	
	public KeyspaceListInitServiceResultDtoTest() {
		
	}
	
	public static KeyspaceListInitServiceResultDto getKeyspaceListServiceCassandraサーバ未構築_未登録() {
		Keyspaces createdKeyspaces = Keyspaces.createEmptyInstance();
		return new KeyspaceListInitServiceResultDto(createEmptyKeyspaceRegistPlans(), createdKeyspaces);
	}
	
	public static KeyspaceListInitServiceResultDto getKeyspaceListServiceCassandraサーバ構築済_登録済() {
		List<KeyspaceRegistPlan> keyspaceRegistPlanList = new ArrayList<KeyspaceRegistPlan>();
		KeyspaceRegistPlan keyspaceRegistPlan = new KeyspaceRegistPlan();
		keyspaceRegistPlan.setKeyspace("keyspace");
		keyspaceRegistPlan.setUserId("aaa");
		keyspaceRegistPlanList.add(keyspaceRegistPlan);
		KeyspaceRegistPlans keyspaceRegistPlans = new KeyspaceRegistPlans(keyspaceRegistPlanList);

		List<Keyspace> keyspaceList = new ArrayList<Keyspace>();
		keyspaceList.add(Keyspace.createInstance("keyspace"));
		Keyspaces createdKeyspaces = Keyspaces.createInstance(keyspaceList);

		return new KeyspaceListInitServiceResultDto(keyspaceRegistPlans, createdKeyspaces);
	}
	
}
