package com.dbaas.cassandra.domain.user;

import com.dbaas.cassandra.domain.user.dto.RegistUserResultDto;
import com.dbaas.cassandra.domain.message.MessageSourceService;
import com.dbaas.cassandra.domain.table.serverByUser.ServerByUserDao;
import com.dbaas.cassandra.domain.table.user.UserDao;
import com.dbaas.cassandra.shared.validation.ValidateResult;
import com.dbaas.cassandra.shared.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.SmartValidator;

import static com.dbaas.cassandra.domain.message.Message.MSG003E;
import static com.dbaas.cassandra.domain.user.LoginUser.createEmptyLoginUser;
import static com.dbaas.cassandra.utils.StringUtils.isEmpty;

@Service
@Transactional
public class UserService {

	private UserDao userDao;
	
    private ServerByUserDao serverByUserDao;

	private Validator validator;

    @Autowired
    public UserService(UserDao userDao, ServerByUserDao serverByUserDao, SmartValidator smartValidator,
					   MessageSourceService messageSource){
    	this.userDao = userDao;
        this.serverByUserDao = serverByUserDao;
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
		RegistUserResultDto result = new RegistUserResultDto(validateResult);
		if (validateResult.hasErrors()){
			return result;
		}

		// 相関チェック
		// 同じユーザーIDが登録済でないか判定
		LoginUser registedUser = findUserByUserId(user.getUserId());
		if (!registedUser.isEmpty()){
			validateResult.addError(MSG003E);
			return result;
		}

		return result;
	}

	/**
	 * 引数で指定したユーザーを登録する
	 *
	 * @param user ユーザー
	 */
	public void regist(User user) {
		// 登録処理
		// TODO insertのみ実行 or 排他制御を実装する
		userDao.registUser(user);
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
