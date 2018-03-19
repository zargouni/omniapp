package com.omniacom.omniapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	
	private UserRepository userRepo;
	
	@Autowired
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	

	public boolean zohoAuthTokenExists(User user) {
		if (user != null)
			if (user.getZohoToken() != null )
				return true;
		return false;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepo.findOneByUserName(userName);
		if (user == null)
			throw new UsernameNotFoundException(userName);
		return new UserDetailsImpl(user);
	}

}
