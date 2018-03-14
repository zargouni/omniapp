package com.omniacom.omniapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.UserRepository;

@Component
public class UserService {

	@Autowired
	UserRepository userRepo;

	public User doLogin(String userName, String password) {
		User user = userRepo.findOneByUserName(userName);
		if (user != null)
			if (user.getPassword().equals(password))
				return user;
		return null;
	}

	public boolean zohoAuthTokenExists(User user) {
		if (user != null)
			if (user.getZohoToken() != null )
				return true;
		return false;
	}

}
