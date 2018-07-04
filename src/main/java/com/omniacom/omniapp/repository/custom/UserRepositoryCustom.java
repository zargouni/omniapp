package com.omniacom.omniapp.repository.custom;

import com.omniacom.omniapp.entity.User;

import java.util.List;

import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Task;;


public interface UserRepositoryCustom {
	
	public User findOneByUserName(String username);
	
	public List<Project> findContributedProjects(User user);
	
	public List<Project> findOwnedProjects(User user);
	
	public List<Task> findAllTasks(User user);
	
	public List<Operation> findContributedOperations(User user);
	
	public List<Task> findCompletedTasks(User user);
	
	public List<Task> findOnGoingTasks(User user);
	
	public boolean addContributingUserToProject(User user, Project project);

	public List<Issue> findAllIssues(User user);

	
	
}
