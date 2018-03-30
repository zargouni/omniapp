package com.omniacom.omniapp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	TaskRepository taskRepo;

	public void addTask(Task task) {
		taskRepo.save(task);
	}
}
