package com.dbaas.cassandra.app.userRegister.service;

import com.dbaas.cassandra.domain.user.dto.RegistUserResultDto;
import com.dbaas.cassandra.domain.user.User;
import com.dbaas.cassandra.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRegisterService {

	private UserService userService;

	@Autowired
	UserRegisterService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 引数で指定したユーザーを登録する
	 *
	 * @param user ユーザー
	 */
	public RegistUserResultDto regist(User user) {

		// 引数で指定したユーザーが登録可能かチェックする
		RegistUserResultDto validateResult = userService.validateForRegist(user);

		// 引数で指定したユーザーを登録する
		userService.regist(user);

		return validateResult;
	}
}
