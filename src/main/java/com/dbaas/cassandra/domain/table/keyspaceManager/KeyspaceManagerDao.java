package com.dbaas.cassandra.domain.table.keyspaceManager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class  KeyspaceManagerDao {
	
    private final KeyspaceManagerRepository repository;

    @Autowired
    public KeyspaceManagerDao(KeyspaceManagerRepository repository){
        this.repository = repository;
    }
	
    /**
     * ユーザーIDで検索
     * 
     * @param userId ユーザーID
     * @return ユーザー
     */
	public List<KeyspaceManagerEntity> findByUserId(String userId) {
		List<KeyspaceManagerEntity> serverList = repository.findByUserId(userId);
		if (serverList == null) {
			return new ArrayList<KeyspaceManagerEntity>();
		}
		return serverList;
	}
	
    /**
     * ユーザーIDで検索
     * 
     * @param userId ユーザーID
     * @return ユーザー
     */
	public List<String> findAllKeyspaceByUserId(LoginUser user) {
		List<String> keyspaceList = new ArrayList<String>();
		List<KeyspaceManagerEntity> entityList = repository.findByUserId(user.getUserId());
		if (entityList == null) {
			return keyspaceList;
		}
		
		for (KeyspaceManagerEntity entity : entityList) {
			keyspaceList.add(entity.keyspace);
		}
		return keyspaceList;
	}
	
	/**
	 * キースペースマネージャーに登録
	 * 
	 * @param user
	 * @param keySpace
	 */
	public void insert(LoginUser user, String keySpace) {
		KeyspaceManagerEntity entity = new KeyspaceManagerEntity();
		entity.userId = user.getUserId();
		entity.keyspace = keySpace;
		repository.save(entity);
	}
	
	/**
	 * キースペースマネージャーに削除
	 * 
	 * @param user
	 * @param keySpace
	 */
	public void delete(LoginUser user, String keySpace) {
		KeyspaceManagerEntity entity = new KeyspaceManagerEntity();
		entity.userId = user.getUserId();
		entity.keyspace = keySpace;
		repository.delete(entity);
	}
}
