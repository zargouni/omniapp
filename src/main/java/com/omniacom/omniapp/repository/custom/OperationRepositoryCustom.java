package com.omniacom.omniapp.repository.custom;

import java.util.List;

import com.omniacom.omniapp.entity.Comment;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Snag;
import com.omniacom.omniapp.entity.User;

public interface OperationRepositoryCustom {

	public List<Service> findAllServices(Operation operation);
	
	public List<Comment> findAllComments(Operation operation);
	
	public List<Snag> findAllSnags(Operation operation);
	
	public List<User> findContributingUsers(Operation operation);
	
}
