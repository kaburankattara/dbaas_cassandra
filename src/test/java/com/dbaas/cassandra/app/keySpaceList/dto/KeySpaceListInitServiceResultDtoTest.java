package com.dbaas.cassandra.app.keySpaceList.dto;

import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlan;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans;

import java.util.ArrayList;
import java.util.List;

import static com.dbaas.cassandra.consts.SysConsts.EMPTY;
import static com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;

public class KeySpaceListInitServiceResultDtoTest {
	
	public KeySpaceListInitServiceResultDtoTest() {
		
	}
	
	public static KeySpaceListInitServiceResultDto getKeySpaceListServiceCassandraサーバ未構築_未登録() {
		List<String> createdKeyspaceList = new ArrayList<String>();
		String endPoint = EMPTY;
		return new KeySpaceListInitServiceResultDto(createEmptyKeyspaceRegistPlans(), createdKeyspaceList, endPoint);
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

		String endPoint = "127.0.0.1";

		return new KeySpaceListInitServiceResultDto(keyspaceRegistPlans, createdKeyspaceList, endPoint);
	}
	
}
