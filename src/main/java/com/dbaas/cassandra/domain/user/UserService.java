package com.dbaas.cassandra.domain.user;

import static com.dbaas.cassandra.domain.user.LoginUser.createEmptyLoginUser;
import static com.dbaas.cassandra.utils.StringUtils.isEmpty;

import com.dbaas.cassandra.shared.exception.SystemException;
import com.dbaas.cassandra.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.table.serverByUser.ServerByUserDao;
import com.dbaas.cassandra.domain.table.user.UserDao;

@Service
@Transactional
public class UserService {

	private UserDao userDao;
	
    private ServerByUserDao serverByUserDao;

    @Autowired
    public UserService(UserDao userDao, ServerByUserDao serverByUserDao){
    	this.userDao = userDao;
        this.serverByUserDao = serverByUserDao;
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
	 * ユーザーを登録する
	 *
	 * @param user ユーザー
	 */
	public void registUser(User user) {
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
