package com.dbaas.cassandra.domain.table.keyspaceRegistPlan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "t_keyspace_regist_plan")
@IdClass(KeyspaceRegistPlanEntityKey.class)
@Entity
public class KeyspaceRegistPlanEntity implements Serializable {

	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "user_id")
	private String userId;
	
	@Id
	@Column(name = "keyspace")
	private String keyspace;
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getKeyspace() {
		return keyspace;
	}
	
	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
	
}