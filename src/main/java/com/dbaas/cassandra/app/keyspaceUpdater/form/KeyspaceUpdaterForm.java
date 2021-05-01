package com.dbaas.cassandra.app.keyspaceUpdater.form;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;

public class KeyspaceUpdaterForm {
	
	public String keyspace;

	public String getKeyspace() {
		return keyspace;
	}

	public Keyspace toKeyspace() {
		return Keyspace.createInstance(keyspace);
	}
	
	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
}
