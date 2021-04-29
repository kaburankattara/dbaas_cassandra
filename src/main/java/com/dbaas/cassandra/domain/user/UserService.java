package com.dbaas.cassandra.domain.user;

import static com.dbaas.cassandra.domain.message.Message.MSG003E;
import static com.dbaas.cassandra.domain.user.LoginUser.createEmptyLoginUser;
import static com.dbaas.cassandra.utils.StringUtils.isEmpty;

import com.dbaas.cassandra.app.userRegister.dto.RegistUserResultDto;
import com.dbaas.cassandra.domain.message.MessageSourceService;
import com.dbaas.cassandra.shared.validation.ValidateResult;
import com.dbaas.cassandra.shared.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.table.serverByUser.ServerByUserDao;
import com.dbaas.cassandra.domain.table.user.UserDao;
import org.springframework.validation.SmartValidator;

@Service
@Transactional
public class UserService {

	private UserDao userDao;
	
    private ServerByUserDao serverByUserDao;

	private MessageSourceService messageSource;

	private Validator validator;

    @Autowired
    public UserService(UserDao userDao, ServerByUserDao serverByUserDao, SmartValidator smartValidator,
					   MessageSourceService messageSource){
    	this.userDao = userDao;
        this.serverByUserDao = serverByUserDao;
		this.messageSource = messageSource;
		this.validator = Validator.getInstance(smartValidator, messageSource);
    }
	
	/**
	 * ユーザーIDからユーザーを取得
	 * 
	 * @param userId ユーザーID
	 * @return ユーザー
	 */
	public LoginUser findUserByUserId(String userId) {
		// ユーザーIDが空の場合、空オブジェクトを返す
		if (isEmpty(userId)) {
			return createEmptyLoginUser();
		}
		return new LoginUser(userDao.findById(userId));
	}

	/**
	 * 引数で指定したユーザーが登録可能かチェックする
	 *
	 * @param user ユーザー
	 * @return 結果
	 */
	public RegistUserResultDto validateForRegist(User user) {
		// 単項目チェック
		ValidateResult validateResult = user.validate(validator);
		if (validateResult.hasErrors()){
			RegistUserResultDto result = new RegistUserResultDto();
			result.setValidateResult(validateResult);
			return result;
		}

		// 相関チェック
		// 同じユーザーIDが登録済でないか判定
		LoginUser registedUser = findUserByUserId(user.getUserId());
		if (!registedUser.isEmpty()){
			RegistUserResultDto result = new RegistUserResultDto();
			result.setValidateResult(validateResult);
			validateResult.addError(MSG003E);
			return result;
		}

		return RegistUserResultDto.createEmptyInstance();
	}

	/**
	 * 引数で指定したユーザーを登録する
	 *
	 * @param user ユーザー
	 */
	public RegistUserResultDto regist(User user) {

		// 登録処理
		// TODO insertのみ実行 or 排他制御を実装する
		userDao.registUser(user);

		return RegistUserResultDto.createEmptyInstance();
	}
	
//    /**
//     * サーバを保有しているか判定
//     * 
//     * @param user ユーザー
//     * @return 判定結果
//     */
//	public boolean hasServer(LoginUser user) {
//		return !serverByUserDao.findByUserId(user.getUserId()).isEmpty();
//	}
}
