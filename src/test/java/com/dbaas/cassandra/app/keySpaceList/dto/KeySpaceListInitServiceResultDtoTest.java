package com.dbaas.cassandra.app.keySpaceList.dto;

import java.util.ArrayList;
import java.util.List;

public class KeySpaceListInitServiceResultDtoTest {
	
	public KeySpaceListInitServiceResultDtoTest() {
		
	}
	
	public static KeySpaceListInitServiceResultDto getKeySpaceListServiceCassandraサーバ未構築_未登録() {
		List<String> keyspaceList = new ArrayList<String>();
		List<String> createdKeyspaceList = new ArrayList<String>();
		return new KeySpaceListInitServiceResultDto(keyspaceList, createdKeyspaceList);
	}
	
	public static KeySpaceListInitServiceResultDto getKeySpaceListServiceCassandraサーバ構築済_登録済() {
		List<String> keyspaceList = new ArrayList<String>();
		keyspaceList.add("keyspace");
		List<String> createdKeyspaceList = new ArrayList<String>();
		createdKeyspaceList.add("keyspace");
		return new KeySpaceListInitServiceResultDto(keyspaceList, createdKeyspaceList);
	}
	
}
