package com.dbaas.cassandra.domain.table.serverByUser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class  ServerByUserDao {
	
    private final ServerByUserRepository repository;

    @Autowired
    public ServerByUserDao(ServerByUserRepository repository){
        this.repository = repository;
    }
	
    /**
     * ユーザーIDで検索
     * 
     * @param userId ユーザーID
     * @return ユーザー
     */
	public List<ServerByUserEntity> findByUserId(String userId) {
		List<ServerByUserEntity> serverList = repository.findByUserId(userId);
		if (serverList == null) {
			return new ArrayList<ServerByUserEntity>();
		}
		return serverList;
	}
}
