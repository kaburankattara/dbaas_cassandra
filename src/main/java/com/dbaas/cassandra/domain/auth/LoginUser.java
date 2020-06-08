package com.dbaas.cassandra.domain.auth;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dbaas.cassandra.domain.user.User;

/**
 * 認証情報クラス
 *
 */
public class LoginUser extends User implements UserDetails {
	
	/**
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

//	/**
//	 * ユーザー情報
//	 */
//	private final User user;

	/**
	 * 権限情報
	 */
	private final Set<GrantedAuthority> authorities;

	/**
	 * コンストラクタ
	 * 
	 * @param user ユーザー
	 * @param authorities 権限
	 */
	public LoginUser(User user, Set<GrantedAuthority> authorities) {
		super(user);
		this.authorities = authorities;
	}

	/**
	 * 権限情報を取得します。
	 */
	@Override
	public Set<GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	/**
	 * パスワードを取得します。
	 */
	@Override
	public String getPassword() {
		return super.getPassword();
	}

	/**
	 * ユーザーIDを取得します。
	 */
	@Override
	public String getUsername() {
		return super.getUserId();
	}

	/**
	 * アカウントが有効か判定します。
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * アカウントがロックされていないか判定します。
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * パスワードが有効か判定します。
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * ユーザが有効か判定します。
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	/**
	 * ユーザーIDを取得
	 * 
	 * @return ユーザーID
	 */
	public String getUserId() {
		return super.getUserId();
	}
	
	/**
	 * ec2インスタンス名のプレフィックス用のユーザーIDを取得
	 * 
	 * @return ユーザーID
	 */
	public String getUserIdForInstanceNamePrefix() {
		return super.getUserId() + "_";
	}

	/**
	 * 権限の有無チェック
	 * @param kengen 権限
	 * @return 判定結果
	 */
	public boolean hasKengen(String kengen) {
		return this.authorities.contains(new SimpleGrantedAuthority(kengen));
	}

}
