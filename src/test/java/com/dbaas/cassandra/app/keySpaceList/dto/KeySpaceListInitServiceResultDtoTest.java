package com.dbaas.cassandra.app.keySpaceList.dto;

import static com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;

import java.util.ArrayList;
import java.util.List;

import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlan;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans;

public class KeySpaceListInitServiceResultDtoTest {
	
	public KeySpaceListInitServiceResultDtoTest() {
		
	}
	
	public static KeySpaceListInitServiceResultDto getKeySpaceListServiceCassandraサーバ未構築_未登録() {
		List<String> createdKeyspaceList = new ArrayList<String>();
		return new KeySpaceListInitServiceResultDto(createEmptyKeyspaceRegistPlans(), createdKeyspaceList);
	}
	
	public static KeySpaceListInitServiceResultDto getKeySpaceListServiceCassandraサーバ構築済_登録済() {
		List<KeyspaceRegistPlan> keyspaceRegistPlanList = new ArrayList<KeyspaceRegistPlan>();
		KeyspaceRegistPlan keyspaceRegistPlan = new KeyspaceRegistPlan();
		keyspaceRegistPlan.setKeyspace("keyspace");
		keyspaceRegistPlan.setUserId("aaa");
		keyspaceRegistPlanList.add(new KeyspaceRegistPlan(keyspaceRegistPlan));
		KeyspaceRegistPlans keyspaceRegistPlans = new KeyspaceRegistPlans(keyspaceRegistPlanList);

		List<String> createdKeyspaceList = new ArrayList<String>();
		createdKeyspaceList.add("keyspace");

		return new KeySpaceListInitServiceResultDto(keyspaceRegistPlans, createdKeyspaceList);
	}
	
}
