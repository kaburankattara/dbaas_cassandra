package com.dbaas.cassandra.domain.user;

import com.dbaas.cassandra.domain.table.serverByUser.ServerByUserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class  UserService {
	
    private final ServerByUserDao serverByUserDao;

    @Autowired
    public UserService(ServerByUserDao serverByUserDao){
        this.serverByUserDao = serverByUserDao;
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
