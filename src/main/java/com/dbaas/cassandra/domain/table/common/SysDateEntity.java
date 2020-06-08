package com.dbaas.cassandra.domain.table.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class SysDateEntity implements Serializable {

	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "sys_date")
	public LocalDateTime sysDate;
	
}