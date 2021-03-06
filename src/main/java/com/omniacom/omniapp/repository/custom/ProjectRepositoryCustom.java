package com.omniacom.omniapp.repository.custom;

import java.util.List;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;

public interface ProjectRepositoryCustom {

	public List<Operation> findAllOperations(Project project);
	
	public List<Issue> findAllIssues(Project project);
		
	public List<Service> findAllServices(Project project);
	
	public List<Service> findAllServicesEvenDeleted(Project project);
	
	public List<User> findContributingUsers(Project project);
	
	public List<Task> findAllCompletedTasks(Project project);
	
	public List<Task> findAllTasks(Project project);

	public List<Task> findAllOnGoingTasks(Project project);
	
	public List<BillOfQuantities> findAllBoqs(Project project);
	
	public Integer findProjectUnassignedTasksCount(Project project);
	
	public Integer findProjectUnplanifiedTasksCount(Project project);
	
	public Integer findProjectOverdueTasksCount(Project project);
	
	public Integer findProjectUnassignedIssuesCount(Project project);
	
	public Integer findProjectOverdueIssuesCount(Project project);
	
	public List<Project> getUnsyncProjects();
	
	public List<Project> getSyncProjects();
	
	public List<Operation> findAllUnsyncedOperations(Project project);
	

}
