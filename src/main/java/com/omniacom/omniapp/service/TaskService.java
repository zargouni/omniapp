package com.omniacom.omniapp.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.repository.TaskRepository;

import net.sf.json.JSONObject;

@Service
public class TaskService {

	@Autowired
	TaskRepository taskRepo;

	public void addTask(Task task) {
		taskRepo.save(task);
	}
	
	public Task findOne(long id) {
		return taskRepo.findOne(id);
	}
	
	public boolean updateTask(long taskId, Task taskCopy) {
		Task task = taskRepo.findOne(taskId);
		if (task != null) {
			if (!task.getName().equals(taskCopy.getName()))
				task.setName(taskCopy.getName());
			if (!task.getStartDate().equals(taskCopy.getStartDate()))
				task.setStartDate(taskCopy.getStartDate());
			if (!task.getEndDate().equals(taskCopy.getEndDate()))
				task.setEndDate(taskCopy.getEndDate());
			if (!task.getStatus().equals(taskCopy.getStatus()))
				task.setStatus(taskCopy.getStatus());
			if (!task.getPriority().equals(taskCopy.getPriority()))
				task.setPriority(taskCopy.getPriority());
			if (!(task.getEstimationRH() == taskCopy.getEstimationRH()))
				task.setEstimationRH(taskCopy.getEstimationRH());
			if (!(task.getEstimationTime() == taskCopy.getEstimationTime()))
				task.setEstimationTime(taskCopy.getEstimationTime());
			taskRepo.save(task);
			return true;
		}
		return false;
	}
	
	public JSONObject jsonTask(Task task) {
		return new JSONObject()
				.element("id", task.getId())
				.element("name", task.getName())
				.element("startDate",  new SimpleDateFormat("dd/MM/yyyy").format(task.getStartDate()))
				.element("endDate",  new SimpleDateFormat("dd/MM/yyyy").format(task.getEndDate()))
				.element("estimationHR", task.getEstimationRH())
				.element("estimationTime", task.getEstimationTime())
				.element("priority", task.getPriority())
				.element("status", task.getStatus());
	}
	
	public JSONObject jsonTaskFormattedDates(Task task) {
		return new JSONObject()
				.element("id", task.getId())
				.element("name", task.getName())
				.element("startDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(new Date()))
				.element("endDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(new Date()))
				.element("estimationHR", task.getEstimationRH())
				.element("estimationTime", task.getEstimationTime())
				.element("priority", task.getPriority())
				.element("status", task.getStatus());
	}

	public JSONObject getTaskParents(long taskId) {
		Task task = taskRepo.findOne(taskId);
		com.omniacom.omniapp.entity.Service service = task.getService();
		if(service.getOperation() != null)
			return new JSONObject()
					.element("service", task.getService().getName())
					.element("operation", task.getService().getOperation().getName())
					.element("project", task.getService().getOperation().getProject().getName());
		else
			return new JSONObject()
					.element("service", task.getService().getName())
					.element("operation", "none")
					.element("project", task.getService().getProject().getName());
	}
}
