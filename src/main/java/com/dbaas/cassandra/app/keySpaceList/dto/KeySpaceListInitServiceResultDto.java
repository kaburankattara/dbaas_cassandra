package com.dbaas.cassandra.app.keySpaceList.dto;

import java.util.List;

public class KeySpaceListInitServiceResultDto {
	
	public KeySpaceListInitServiceResultDto() {
		
	}
	
	public KeySpaceListInitServiceResultDto(List<String> keyspaceList, List<String> createdKeyspaceList) {
		this.keyspaceList = keyspaceList;
		this.createdKeyspaceList = createdKeyspaceList;
	}	
	
	public List<String> keyspaceList;

	public List<String> createdKeyspaceList;
	
	public List<String> getKeyspaceList() {
		return keyspaceList;
	}
	
	public void setKeyspaceList(List<String> keyspaceList) {
		this.keyspaceList = keyspaceList;
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
