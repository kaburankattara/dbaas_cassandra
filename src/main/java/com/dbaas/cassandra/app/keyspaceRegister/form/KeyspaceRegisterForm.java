package com.dbaas.cassandra.app.keyspaceRegister.form;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;

public class KeyspaceRegisterForm {
	
	public String keyspaceName;

	public String getKeyspaceName() {
		return keyspaceName;
	}

	public Keyspace getKeyspace() {
		return Keyspace.createInstance(keyspaceName);
	}
	
	public void setKeyspaceName(String keyspaceName) {
		this.keyspaceName = keyspaceName;
	}
}
