package com.dbaas.cassandra.app.keySpaceList.dto;

import java.util.List;

import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans;

public class KeySpaceListInitServiceResultDto {
	
	public KeySpaceListInitServiceResultDto() {
		
	}
	
	public KeySpaceListInitServiceResultDto(KeyspaceRegistPlans keyspaceRegistPlans, List<String> createdKeyspaceList) {
		this.keyspaceRegistPlans = keyspaceRegistPlans;
		this.createdKeyspaceList = createdKeyspaceList;
	}	
	
	public KeyspaceRegistPlans keyspaceRegistPlans;

	public List<String> createdKeyspaceList;
	
	public List<String> getKeyspaceList() {
		return keyspaceRegistPlans.getKeyspaceList();
	}

	public KeyspaceRegistPlans getKeyspaceRegistPlans() {
		return keyspaceRegistPlans;
	}
	
	public void setKeyspaceRegistPlans(KeyspaceRegistPlans keyspaceRegistPlans) {
		this.keyspaceRegistPlans = keyspaceRegistPlans;
	}

	public List<String> getCreatedKeyspaceList() {
		return createdKeyspaceList;
	}
	
	public void setCreatedKeyspaceList(List<String> createdKeyspaceList) {
		this.createdKeyspaceList = createdKeyspaceList;
	}
	
	/**
	 * 引数で指定されたキースペースが一覧画面でリンク表示可能か判定
	 * 
	 * @param keyspace
	 * @return
	 */
	public String getAppendClassForStatusComplete(String keyspace) {
		return createdKeyspaceList.contains(keyspace) ? "" : "displayNone";
	}
	
	public String getAppendClassForStatusPending(String keyspace) {
		return !createdKeyspaceList.contains(keyspace) ? "" : "displayNone";
	}
	
}
