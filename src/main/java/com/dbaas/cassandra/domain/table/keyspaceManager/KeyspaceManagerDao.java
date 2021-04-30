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
	 * キースペースマネージャーに登録
	 * 
	 * @param user
	 * @param keyspace
	 */
	public void insert(LoginUser user, String keyspace) {
		KeyspaceManagerEntity entity = new KeyspaceManagerEntity();
		entity.setUserId(user.getUserId());
		entity.setKeyspace(keyspace);
		repository.save(entity);
	}
	
	/**
	 * キースペースマネージャーに削除
	 * 
	 * @param user
	 * @param keyspace
	 */
	public void delete(LoginUser user, String keyspace) {
		KeyspaceManagerEntity entity = new KeyspaceManagerEntity();
		entity.setUserId(user.getUserId());
		entity.setKeyspace(keyspace);
		repository.delete(entity);
	}
}
