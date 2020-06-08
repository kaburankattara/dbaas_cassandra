package com.dbaas.cassandra.domain.user;

import com.dbaas.cassandra.domain.table.user.UserEntity;
import com.dbaas.cassandra.utils.StringUtils;

/**
 * ユーザー
 * 
 */
public class User extends UserEntity {
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * デフォルトコンストラクタ
	 */
	public User() {
	}

	/**
	 * デフォルトコンストラクタ
	 */
	public User(UserEntity entity) {
		this.setUserId(entity.getUserId());
		this.setPassword(entity.getPassword());
		this.setUserName(entity.getUserName());
	}

	/**
	 * ユーザーが空か判定
	 * 
	 * @return 判定結果
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(getUserId());
	}
 }
