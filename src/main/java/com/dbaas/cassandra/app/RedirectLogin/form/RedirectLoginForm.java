package com.dbaas.cassandra.app.RedirectLogin.form;

public class RedirectLoginForm {

	public RedirectLoginForm() {
	}

	public RedirectLoginForm(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}
	
	public String userId;

	public String password;
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}
