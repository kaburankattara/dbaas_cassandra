package com.dbaas.cassandra.app.keyspaceList.dto;

import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlan;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;

import java.util.ArrayList;
import java.util.List;

import static com.dbaas.cassandra.consts.SysConsts.EMPTY;
import static com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;

public class KeyspaceListInitServiceResultDtoTest {
	
	public KeyspaceListInitServiceResultDtoTest() {
		
	}
	
	public static KeyspaceListInitServiceResultDto getKeyspaceListServiceCassandraサーバ未構築_未登録() {
		List<String> createdKeyspaceList = new ArrayList<String>();
		String endPoint = EMPTY;
		return new KeyspaceListInitServiceResultDto(createEmptyKeyspaceRegistPlans(), createdKeyspaceList, endPoint);
	}
	
	public static KeyspaceListInitServiceResultDto getKeyspaceListServiceCassandraサーバ構築済_登録済() {
		List<KeyspaceRegistPlan> keyspaceRegistPlanList = new ArrayList<KeyspaceRegistPlan>();
		KeyspaceRegistPlan keyspaceRegistPlan = new KeyspaceRegistPlan();
		keyspaceRegistPlan.setKeyspace("keyspace");
		keyspaceRegistPlan.setUserId("aaa");
		keyspaceRegistPlanList.add(keyspaceRegistPlan);
		KeyspaceRegistPlans keyspaceRegistPlans = new KeyspaceRegistPlans(keyspaceRegistPlanList);

		List<String> createdKeyspaceList = new ArrayList<String>();
		createdKeyspaceList.add("keyspace");

		String endPoint = "127.0.0.1";

		return new KeyspaceListInitServiceResultDto(keyspaceRegistPlans, createdKeyspaceList, endPoint);
	}
	
}
