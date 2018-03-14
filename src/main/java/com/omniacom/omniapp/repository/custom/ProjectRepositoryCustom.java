package com.omniacom.omniapp.repository.custom;

import java.util.List;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.User;

public interface ProjectRepositoryCustom {

	public List<Operation> findAllOperations(Project project);
	
	public List<User> findContributingUsers(Project project);
}
