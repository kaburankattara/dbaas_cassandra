package com.dbaas.cassandra.app.userRegister.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.userRegister.dto.RegistUserResultDto;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.domain.user.User;
import com.dbaas.cassandra.domain.user.UserService;

@Service
@Transactional
public class UserRegisterService {

	private UserService userService;

	@Autowired
	UserRegisterService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * ユーザーを登録する
	 *
	 * @param user ユーザー
	 */
	public RegistUserResultDto registUser(User user) {
		// 単項目チェック
		// TODO 実装する

		// 相関チェック
		// 同じユーザーIDが登録済でないか判定
		LoginUser registedUser = userService.findUserByUserId(user.getUserId());
		if (!registedUser.isEmpty()){
			RegistUserResultDto result = new RegistUserResultDto();
			result.setIsRegistedUser();
			return result;
		}

		// 登録処理
		// TODO insertのみ実行 or 排他制御を実装する
		userService.registUser(user);

		return RegistUserResultDto.createEmptyInstance();
	}
}
