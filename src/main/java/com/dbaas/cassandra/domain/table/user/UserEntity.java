package com.dbaas.cassandra.domain.table.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "m_User")
@Entity
public class UserEntity implements Serializable {
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * デフォルトコンストラクタ
	 */
	public UserEntity() {
	}

    /**
     * ユーザーID
     */
	@Id
    private String userId;

    /**
     * ユーザー名
     */
    private String userName;

    /**
     * パスワード
     */
    private String password;

	/**
	 * ユーザーIDを取得
	 * @return ユーザーID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * ユーザーIDを設定
	 * @param userId ユーザーID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * ユーザー名を取得
	 * @return ユーザー名
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * ユーザー名を設定
	 * @param userName ユーザー名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * パスワードを取得
	 * @return パスワード
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * パスワードを設定
	 * @param password パスワード
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
