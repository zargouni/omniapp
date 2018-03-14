package com.omniacom.omniapp.repository.custom;

import com.omniacom.omniapp.entity.User;

import java.util.List;

import com.omniacom.omniapp.entity.Project;;


public interface UserRepositoryCustom {
	
	public User findOneByUserName(String username);
	
	public boolean updateLocalUserFromZoho(User user,String email, String password);
	
	public List<Project> findContributedProjects(User user);
	
	public List<Project> findOwnedProjects(User user);
	
	
}
