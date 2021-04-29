package com.dbaas.cassandra.domain.table.user;

import com.dbaas.cassandra.shared.validation.constraints.MaxLength;
import com.dbaas.cassandra.shared.validation.constraints.Required;

import java.io.Serializable;

import javax.persistence.Entity;
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
	 * ユーザーID 最大桁数
	 */
	private static final int USER_ID_MAX_LENGTH = 10;

	/**
	 * ユーザー名 最大桁数
	 */
	private static final int USER_NAME_MAX_LENGTH = 10;

	/**
	 * デフォルトコンストラクタ
	 */
	public UserEntity() {
	}

    /**
     * ユーザーID
     */
	@Id
	@Required(message = "ユーザーIDの入力は必須です。")
	@MaxLength(max = USER_ID_MAX_LENGTH, message = "ユーザーIDは" + USER_ID_MAX_LENGTH + "文字以下で入力してください。")
    private String userId;

    /**
     * ユーザー名
     */
	@Required(message = "ユーザー名の入力は必須です。")
	@MaxLength(max = USER_NAME_MAX_LENGTH, message = "ユーザー名は" + USER_NAME_MAX_LENGTH + "文字以下で入力してください。")
    private String userName;

    /**
     * パスワード
     */
	@Required(message = "パスワードの入力は必須です。")
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
