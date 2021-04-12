package com.dbaas.cassandra.domain.user;

import com.dbaas.cassandra.domain.table.user.UserEntity;
import com.dbaas.cassandra.utils.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	 * パスワードエンコーダー
	 */
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/**
	 * デフォルトコンストラクタ
	 */
	public User() {
	}

	/**
	 * コンストラクタ
	 */
	public User(UserEntity entity) {
		this.setUserId(entity.getUserId());
		this.setPassword(entity.getPassword());
		this.setUserName(entity.getUserName());
	}

	/**
	 * コンストラクタ
	 */
	public User(String userId, String userName, String rawPassword) {
		this.setUserId(userId);
		this.setUserName(userName);
		this.setPassword(passwordEncoder.encode(rawPassword));
	}

	/**
	 * 空ユーザーを作成する
	 * 
	 * @return ユーザー
	 */
	public static User createEmptyUser() {
		return new User(new UserEntity());
	}

	/**
	 * ユーザーが空か判定
	 * 
	 * @return 判定結果
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(getUserId());
	}

	/**
	 * ユーザーEntityを作成する
	 *
	 * @return ユーザーEntity
	 */
	public UserEntity createEntity() {
		UserEntity entity = new UserEntity();
		entity.setUserId(getUserId());
		entity.setUserName(getUserName());
		entity.setPassword(getPassword());
		return entity;
	}
 }
