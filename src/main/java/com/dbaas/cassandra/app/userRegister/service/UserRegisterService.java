package com.dbaas.cassandra.app.userRegister.service;

import com.dbaas.cassandra.app.userRegister.dto.RegistUserResultDto;
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
		userService.validateForRegist(user);

		// 引数で指定したユーザーを登録する
		// TODO insertのみ実行 or 排他制御を実装する
		userService.regist(user);

		return RegistUserResultDto.createEmptyInstance();
	}
}
