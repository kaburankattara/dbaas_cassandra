package com.dbaas.cassandra.domain.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.domain.user.User;
import com.dbaas.cassandra.domain.user.UserService;

@Service
@Transactional(readOnly = true)
public class AuthenticationUserDetailsService implements UserDetailsService {

	UserService userService;
	
	@Autowired
	AuthenticationUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = userService.findUserByUserId(userId);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("");
		}
		return new LoginUser(user);
	}
	
//	public Set<GrantedAuthority> getAuthorities(LoginUser user) {
//		return AuthorityUtils.createAuthorityList("").stream()
//				.map(this::conv).flatMap(x -> x.stream()).collect(toSet()));
//	}
	
}
