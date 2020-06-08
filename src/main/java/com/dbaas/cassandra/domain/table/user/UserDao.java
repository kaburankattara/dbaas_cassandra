package com.dbaas.cassandra.domain.table.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.user.User;

@Service
@Transactional
public class  UserDao {
	
    private final MUserRepository repository;

    public UserDao(MUserRepository repository){
        this.repository = repository;
    }
	
    /**
     * 主キー検索
     * 
     * @param userId ユーザーID
     * @return ユーザー
     */
	public User findById(String id) {
		Optional<UserEntity> user = repository.findById(id);
		if (user == null) {
			return new User();
		}
		return new User(user.get());
	}
}
