package com.dbaas.cassandra.domain.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.table.user.UserDao;
import com.dbaas.cassandra.domain.user.User;

@Service
@Transactional(readOnly = true)
public class AuthenticationUserDetailsService implements UserDetailsService {

	UserDao userDao;
	
	@Autowired
	AuthenticationUserDetailsService(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = userDao.findById(userId);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("");
		}
		return new LoginUser(user, null);
	}
	
//	public Set<GrantedAuthority> getAuthorities(LoginUser user) {
//		return AuthorityUtils.createAuthorityList("").stream()
//				.map(this::conv).flatMap(x -> x.stream()).collect(toSet()));
//	}
	
}
