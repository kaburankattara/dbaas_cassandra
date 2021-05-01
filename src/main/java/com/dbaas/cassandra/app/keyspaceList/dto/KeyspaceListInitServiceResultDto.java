package com.dbaas.cassandra.app.keyspaceList.dto;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;

import java.util.List;

import static com.dbaas.cassandra.utils.StringUtils.isNotEmpty;

public class KeyspaceListInitServiceResultDto {
	
	public KeyspaceListInitServiceResultDto() {
		
	}

	public KeyspaceListInitServiceResultDto(KeyspaceRegistPlans keyspaceRegistPlans, Keyspaces createdKeyspaces, String endPoint) {
		this.keyspaceRegistPlans = keyspaceRegistPlans;
		this.createdKeyspaces = createdKeyspaces;
		setKeyspaceList();
		this.endPoint = endPoint;
	}
	
	public KeyspaceRegistPlans keyspaceRegistPlans;

	public Keyspaces createdKeyspaces;

	public List<String> keyspaceList;

	public String endPoint;
	
	public List<String> getKeyspaceList() {
		return keyspaceList;
	}

	public void setKeyspaceList() {
		this.keyspaceList = keyspaceRegistPlans.getKeyspaceList();
		if (createdKeyspaces.isEmpty()) {
			return;
		}
		for (Keyspace keyspace : createdKeyspaces.getKeyspaceList()) {
			if (!keyspaceList.contains(keyspace)) {
				keyspaceList.add(keyspace.getKeyspace());
			}
		}
	}

	public KeyspaceRegistPlans getKeyspaceRegistPlans() {
		return keyspaceRegistPlans;
	}
	
	public void setKeyspaceRegistPlans(KeyspaceRegistPlans keyspaceRegistPlans) {
		this.keyspaceRegistPlans = keyspaceRegistPlans;
	}

	public Keyspaces getCreatedKeyspaces() {
		return createdKeyspaces;
	}
	
	public void setCreatedKeyspaces(Keyspaces createdKeyspaces) {
		this.createdKeyspaces = createdKeyspaces;
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
		return createdKeyspaces.hasKeyspace(Keyspace.createInstance(keyspace)) ? "" : "displayNone";
	}
	
	public String getAppendClassForStatusPending(String keyspace) {
		return !createdKeyspaces.hasKeyspace(Keyspace.createInstance(keyspace)) ? "" : "displayNone";
	}

	public String getAppendClassForEndPoint() {
		return isNotEmpty(endPoint) ? "" : "displayNone";
	}

	public boolean hasKeyspaceList() {
		return !createdKeyspaces.isEmpty();
	}
	
}
