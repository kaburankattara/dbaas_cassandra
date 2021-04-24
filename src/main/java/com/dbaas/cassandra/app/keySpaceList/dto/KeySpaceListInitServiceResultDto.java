package com.dbaas.cassandra.app.keySpaceList.dto;

import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans;
import com.dbaas.cassandra.utils.ObjectUtils;

import java.util.List;

import static com.dbaas.cassandra.utils.StringUtils.isNotEmpty;

public class KeySpaceListInitServiceResultDto {
	
	public KeySpaceListInitServiceResultDto() {
		
	}

	public KeySpaceListInitServiceResultDto(KeyspaceRegistPlans keyspaceRegistPlans, List<String> createdKeyspaceList, String endPoint) {
		this.keyspaceRegistPlans = keyspaceRegistPlans;
		this.createdKeyspaceList = createdKeyspaceList;
		setKeyspaceList();
		this.endPoint = endPoint;
	}
	
	public KeyspaceRegistPlans keyspaceRegistPlans;

	public List<String> createdKeyspaceList;

	public List<String> keyspaceList;

	public String endPoint;
	
	public List<String> getKeyspaceList() {
		return keyspaceList;
	}

	public void setKeyspaceList() {
		this.keyspaceList = keyspaceRegistPlans.getKeyspaceList();
		if (ObjectUtils.isEmpty(createdKeyspaceList)) {
			return;
		}
		for (String keyspace : createdKeyspaceList) {
			if (!createdKeyspaceList.contains(keyspace)) {
				keyspaceList.add(keyspace);
			}
		}
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

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public boolean hasEndPoint() {
		return isNotEmpty(endPoint);
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

	public String getAppendClassForEndPoint() {
		return isNotEmpty(endPoint) ? "" : "displayNone";
	}

	public boolean hasKeyspaceList() {
		return ObjectUtils.isNotEmpty(createdKeyspaceList);
	}
	
}
