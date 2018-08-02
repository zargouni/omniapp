package com.omniacom.omniapp.zohoAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.TaskRepository;
import com.omniacom.omniapp.repository.UserRepository;
import com.omniacom.omniapp.service.ServiceService;
import com.omniacom.omniapp.service.TaskService;
import com.zoho.projects.exception.ProjectsException;

@Component
public class TasksAPI {

	private com.zoho.projects.api.TasksAPI tasksApi;

	@Autowired
	private UsersAPI usersApi;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TaskRepository taskRepo;

	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private TaskService taskService;

	public com.zoho.projects.model.Task pushTask(Task task, String projectId, User user) throws IOException {
		tasksApi = new com.zoho.projects.api.TasksAPI(usersApi.getUserAuthToken(user), usersApi.getPortalId(user));

		com.zoho.projects.model.Task zohoResult = null;
		com.zoho.projects.model.Task zohoTask = Converters.convertTaskToZohoTask(task);
		try {
			// TODO push to zoho + update local task with zoho id
			zohoResult = tasksApi.create(projectId, zohoTask);
			task.setZohoId(zohoResult.getId());
			taskRepo.save(task);

		} catch (ProjectsException pe) {
			System.out.println("Code : "+ pe.getCode() +" Message : "+ pe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return zohoResult;
	}

	public List<Task> pushAllTasks() {
		List<Task> unsyncedTasks = null;
		List<Task> syncedTasks = new ArrayList<Task>();
		List<Service> syncedServices = serviceService.findAllSyncedServices();
		User user = userRepo.findOne(1L);

		System.out.println("Running tasks sync ......");

		for (Service s : syncedServices) {
			unsyncedTasks = serviceService.findAllUnsyncedTasks(s);
			System.out.println("Tasks to sync in TaskList '" + s.getName() + "': " + unsyncedTasks.size());

			for (Task task : unsyncedTasks) {
				try {
					this.pushTask(task, String.valueOf(s.getOperation().getProject().getZohoId()), user);
					syncedTasks.add(task);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return syncedTasks;
	}

	public void updateAllTasks() throws Exception {
		List<Task> syncedTasks = taskService.findAllSyncedTasks();
		User user = userRepo.findOne(1L);
		tasksApi = new com.zoho.projects.api.TasksAPI(usersApi.getUserAuthToken(user), usersApi.getPortalId(user));

		for(Task task : syncedTasks) {
			com.zoho.projects.model.Task zohoTask = tasksApi.get(String.valueOf(task.getService().getOperation().getProject().getZohoId()),
					String.valueOf(Converters.convertTaskToZohoTask(task).getId()));
			
			com.zoho.projects.model.Task zohoLocalTask = Converters.convertTaskToZohoTask(task);
			
			if(!compareZohoTasks(zohoTask, zohoLocalTask)){
				try {
					tasksApi.update(String.valueOf(task.getService().getOperation().getProject().getZohoId()), Converters.convertTaskToZohoTask(task));
				} catch (ProjectsException pe) {
					System.out.println("Code : "+ pe.getCode() +" Message : "+ pe.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
		}
	}
	
	public boolean compareZohoTasks(com.zoho.projects.model.Task newTask, com.zoho.projects.model.Task oldTask) {
		boolean result = true;
		if(!newTask.getName().equals(oldTask.getName()))
			result = false;
//		if(!(newTask.getOwners().containsAll(oldTask.getOwners()) && newTask.getOwners().size() == oldTask.getOwners().size()))
//			result = false;
		if(!newTask.getEndDate().equals(oldTask.getEndDate()))
			result = false;
		if(!newTask.getStartDate().equals(oldTask.getStartDate()))
			result = false;
		
		System.out.println("task start date: "+newTask.getStartDate()+" old task start date: "+oldTask.getStartDate());
		
		return result;
	}
}
