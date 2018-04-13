package com.omniacom.omniapp.repository.custom;

import java.util.List;

import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Task;

public interface ServiceRepositoryCustom {

	List<Task> findAllTasks(Service service);
}
