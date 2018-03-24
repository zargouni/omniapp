package com.omniacom.omniapp.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.service.UserService;


public class UserConverter implements Converter<String, User> {

	@Autowired
	private UserService userService;
	
	@Override
	public User convert(String id) {
		try {
            Long userId = Long.valueOf(id);
            return userService.findById(userId);
        } catch (NumberFormatException e) {
            return null;
        }
	}

}
