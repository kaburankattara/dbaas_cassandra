package com.dbaas.cassandra.domain.table.keyspaceRegistPlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class KeyspaceRegistPlanEntityKey implements Serializable {

	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
	public String userId;
	
	@Column(name = "keyspace")
	public String keyspace;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
