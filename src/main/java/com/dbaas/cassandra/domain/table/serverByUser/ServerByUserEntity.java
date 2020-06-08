package com.dbaas.cassandra.domain.table.serverByUser;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "server_by_user")
@IdClass(ServerByUserEntityKey.class)
@Entity
public class ServerByUserEntity implements Serializable {

	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "user_id")
	public String userId;
	
	@Id
	@Column(name = "server_name")
	public String serverName;
	
}