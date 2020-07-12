package com.dbaas.cassandra.domain.table.keyspaceManager;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "t_keyspace_manager")
@IdClass(KeyspaceManagerEntityKey.class)
@Entity
public class KeyspaceManagerEntity implements Serializable {

	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "user_id")
	public String userId;
	
	@Id
	@Column(name = "keyspace")
	public String keyspace;
	
}