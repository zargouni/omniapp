package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.UploadedFile;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.TaskRepository;
import com.omniacom.omniapp.repository.UserRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class TaskService {

	@Autowired
	TaskRepository taskRepo;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserService userService;
	

	public Task addTask(Task task) {
		return taskRepo.save(task);
	}

	public Task findOne(long id) {
		return taskRepo.findOne(id);
	}

	public boolean updateTask(long taskId, Task taskCopy) {
		Task task = taskRepo.findOne(taskId);
		task.getUsers().clear();
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
		return new JSONObject().element("id", task.getId()).element("name", task.getName())
				.element("startDate", new SimpleDateFormat("dd/MM/yyyy").format(task.getStartDate()))
				.element("endDate", new SimpleDateFormat("dd/MM/yyyy").format(task.getEndDate()))
				.element("estimationHR", task.getEstimationRH()).element("estimationTime", task.getEstimationTime())
				.element("priority", task.getPriority()).element("status", task.getStatus())
				.element("files", findAllFiles(task));
	}

	public JSONObject jsonTaskFormattedDates(Task task) {
		return new JSONObject().element("id", task.getId()).element("name", task.getName())
				.element("startDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(new Date()))
				.element("endDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(new Date()))
				.element("estimationHR", task.getEstimationRH()).element("estimationTime", task.getEstimationTime())
				.element("priority", task.getPriority()).element("status", task.getStatus());
	}

	public JSONObject getTaskParents(long taskId) {
		Task task = taskRepo.findOne(taskId);
		com.omniacom.omniapp.entity.Service service = task.getService();
		if (service.getOperation() != null)
			return new JSONObject().element("service", task.getService().getName())
					.element("operation", task.getService().getOperation().getName())
					.element("project", task.getService().getOperation().getProject().getName());
		else
			return new JSONObject().element("service", task.getService().getName()).element("operation", "none")
					.element("project", task.getService().getProject().getName());
	}

	public boolean addOneOwner(Task task, User user) {
		return taskRepo.addOneOwner(task, user);
	}

	public JSONArray findAllUsersForTask(long taskId) {
		JSONArray array = new JSONArray();
		Task task = taskRepo.findOne(taskId);
		List<User> taskUsers = taskRepo.findAllUsers(task);
		List<User> users = (List<User>) userRepo.findAll();
		for (User user : users) {
			if (taskUsers.contains(user))
				array.add(userService.jsonUser(user).element("selected", true));
			else
				array.add(userService.jsonUser(user).element("selected", false));
		}
		return array;
	}
	
	public JSONArray findAllFiles(Task task) {
		JSONArray array = new JSONArray();
		List<UploadedFile> files = taskRepo.findAllFiles(task);
		for(UploadedFile file : files) {
			if(!file.isDeleted())
			array.add(new JSONObject()
					.element("name", file.getName())
					.element("id", file.getId())
					.element("location", file.getLocation())
					.element("type", file.getType())
					.element("size", file.getSize())
					.element("creationDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(file.getCreationDate())));
		}
		return array;
	}

	
}
