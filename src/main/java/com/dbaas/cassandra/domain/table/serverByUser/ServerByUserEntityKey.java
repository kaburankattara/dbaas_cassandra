package com.dbaas.cassandra.domain.table.serverByUser;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class ServerByUserEntityKey implements Serializable {

	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
	public String userId;
	
	@Column(name = "server_name")
	public String serverName;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
