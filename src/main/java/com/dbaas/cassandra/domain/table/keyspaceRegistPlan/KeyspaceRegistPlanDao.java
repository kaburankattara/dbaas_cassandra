package com.dbaas.cassandra.domain.table.keyspaceRegistPlan;

import java.util.ArrayList;
import java.util.List;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlan;
import com.dbaas.cassandra.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class KeyspaceRegistPlanDao {
	
    private final KeyspaceRegistPlanRepository repository;

    @Autowired
    public KeyspaceRegistPlanDao(KeyspaceRegistPlanRepository repository){
        this.repository = repository;
    }

	/**
	 * 主キーで検索
	 *
	 * @param user ユーザー
	 * @param keyspace キースペース
	 * @return キースペース登録予定
	 */
	public KeyspaceRegistPlan findByUserIdAndKeyspace(User user, Keyspace keyspace) {
		KeyspaceRegistPlanEntity entity = repository.findByUserIdAndKeyspace(user.getUserId(), keyspace.getKeyspace());
		if (entity == null) {
			return KeyspaceRegistPlan.createEmptyInstance();
		}
		return KeyspaceRegistPlan.createInstance(entity);
	}

    /**
     * ユーザーIDで検索
     * 
     * @param userId ユーザーID
     * @return ユーザー
     */
	public List<KeyspaceRegistPlanEntity> findByUserId(String userId) {
		List<KeyspaceRegistPlanEntity> keyspaceRegistPlanList = repository.findByUserId(userId);
		if (keyspaceRegistPlanList == null) {
			return new ArrayList<KeyspaceRegistPlanEntity>();
		}
		return keyspaceRegistPlanList;
	}
	
	/**
	 * キースペースマネージャーに登録
	 * 
	 * @param user
	 * @param keyspace
	 */
	public void insert(LoginUser user, Keyspace keyspace) {
		KeyspaceRegistPlanEntity entity = new KeyspaceRegistPlanEntity();
		entity.setUserId(user.getUserId());
		entity.setKeyspace(keyspace.getKeyspace());
		repository.save(entity);
	}
	
	/**
	 * キースペースマネージャーに削除
	 * 
	 * @param user
	 * @param keyspace
	 */
	public void delete(LoginUser user, String keyspace) {
		KeyspaceRegistPlanEntity entity = new KeyspaceRegistPlanEntity();
		entity.setUserId(user.getUserId());
		entity.setKeyspace(keyspace);
		repository.delete(entity);
	}
}
