package com.dbaas.cassandra.domain.user;

import com.dbaas.cassandra.domain.table.user.UserEntity;
import com.dbaas.cassandra.shared.validation.ValidateResult;
import com.dbaas.cassandra.shared.validation.Validator;
import com.dbaas.cassandra.utils.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.dbaas.cassandra.consts.SysConsts.EMPTY;

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
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

	/**
	 * クラス名
	 */
	public static final String CLASS_NAME = User.class.getSimpleName();

	/**
	 * 空ユーザーを作成する
	 * 
	 * @return ユーザー
	 */
	public static User createEmptyUser() {
		return new User(new UserEntity());
	}

	/**
	 * 空ユーザーを作成する
	 *
	 * @return ユーザー
	 */
	public static User createInstanceByRawPassword(String userId, String userName, String rawPassword) {
		return new User(userId, userName, rawPassword, null);
	}

	/**
	 * 空ユーザーを作成する
	 *
	 * @return ユーザー
	 */
	public static User createInstanceByPassword(String userId, String userName, String password) {
		return new User(userId, userName, null, password);
	}

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
	public User(String userId, String userName, String rawPassword, String password) {
		this.setUserId(userId);
		this.setUserName(userName);
		this.setPassword(password);
		this.rawPassword = rawPassword;
		// 各パラメーターを設定後、変換後パスワードを再設定する
		this.setPassword(getPassword());
	}

	/**
	 * 変換前パスワード
	 */
	private String rawPassword;

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

	public ValidateResult validate(Validator validator) {
		return validator.validate(this, CLASS_NAME);
	}

	/**
	 * パスワードを取得
	 * @return パスワード
	 */
	@Override
	public String getPassword() {
		// 変換前のパスワードを保持している場合、変換して返却する
		if (StringUtils.isNotEmpty(rawPassword)) {
			return passwordEncoder.encode(rawPassword);
		}

		// 変換後のパスワードを保持している場合、そのまま返却する
		if (StringUtils.isNotEmpty(super.getPassword())) {
			return super.getPassword();
		}

		// 上記以外の場合、空を返す
		return EMPTY;
	}
 }
