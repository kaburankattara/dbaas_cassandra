package com.dbaas.cassandra.domain.table.user;

import java.util.Optional;

import com.dbaas.cassandra.shared.exception.SystemException;
import com.dbaas.cassandra.utils.ObjectUtils;
import io.netty.util.internal.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.user.User;

@Service
@Transactional
public class  UserDao {
	
    private final MUserRepository repository;

	@Autowired
    public UserDao(MUserRepository repository){
        this.repository = repository;
    }
	
    /**
     * 主キー検索
     * 
     * @param userId ユーザーID
     * @return ユーザー
     */
	public User findById(String userId) {
		Optional<UserEntity> user = repository.findById(userId);
		if (user == null) {
			return new User();
		}
		return new User(user.get());
	}

	/**
	 * ユーザーを登録する
	 *
	 * @param user ユーザー
	 */
	public void registUser(User user) {

		if (ObjectUtils.isEmpty(user) || user.isEmpty()) {
			throw new SystemException();
		}

		repository.save(user.createEntity());
	}
}
