package com.dbaas.cassandra.app.userRegister.form;

import com.dbaas.cassandra.app.RedirectLogin.form.RedirectLoginForm;
import com.dbaas.cassandra.domain.user.User;

public class UserRegisterForm {

	public String userId;

	public String userName;

	public String password;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User createUser() {
		return User.createInstanceByRawPassword(userId, userName, password);
	}

	public RedirectLoginForm createRedirectLoginForm() {
		return new RedirectLoginForm(userId, password);
	}

}
