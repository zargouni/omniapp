package com.omniacom.omniapp.repository.custom;

import java.util.List;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;

public interface TaskRepositoryCustom {

	public List<User> findAllUsers(Task task);
}
