package com.dbaas.cassandra.app.userRegister.form;

import com.dbaas.cassandra.domain.cassandra.table.ColumnForm;
import com.dbaas.cassandra.domain.cassandra.table.ColumnsForm;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.user.User;
import com.dbaas.cassandra.utils.NumberUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static com.dbaas.cassandra.utils.NumberUtils.toInt;
import static com.dbaas.cassandra.utils.StringUtils.toLowerCase;

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
		return new User(userId, userName, password);
	}

}
