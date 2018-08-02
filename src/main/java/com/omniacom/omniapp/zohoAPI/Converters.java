package com.omniacom.omniapp.zohoAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.format.datetime.DateFormatter;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.User;
import com.zoho.projects.model.Milestone;
import com.zoho.projects.model.Owner;
import com.zoho.projects.model.Task;
import com.zoho.projects.model.Tasklist;

public class Converters {

	public static com.zoho.projects.model.Project convertLocalProjectToZoho(Project project) {

		com.zoho.projects.model.Project zohoProject = new com.zoho.projects.model.Project();

		zohoProject.setName(project.getName());
		zohoProject.setDescription(project.getDescription());
		zohoProject.setCreatedDate(project.getCreationDate().toString());

		zohoProject.setOwnerId(String.valueOf(project.getOwner().getZohoId()));
		return zohoProject;
	}

	public static Milestone convertOperationToMilestone(Operation operation) {
		Milestone milestone = new Milestone();
		milestone.setName(operation.getName());
		if (operation.getZohoId() != 0)
			milestone.setId(operation.getZohoId());
		milestone.setStartDate(new DateFormatter("MM-dd-YYYY").print(operation.getStartDate(), Locale.ENGLISH));

		milestone.setEndDate(new DateFormatter("MM-dd-YYYY").print(operation.getEndDate(), Locale.ENGLISH));

		milestone.setFlag("internal");
		milestone.setOwnerId(operation.getResponsible().getZohoId());
		return milestone;
	}

	public static Tasklist convertServiceToTaskList(Service service) {
		Tasklist taskList = new Tasklist();
		taskList.setName(service.getName());
		if (service.getZohoId() != 0)
			taskList.setId(service.getZohoId());
		taskList.setMilestone(convertOperationToMilestone(service.getOperation()));
		taskList.setCreatedTime(new DateFormatter("MM-dd-YYYY").print(service.getCreationDate(), Locale.ENGLISH));
		taskList.setFlag("internal");
		return taskList;
	}

	public static Task convertTaskToZohoTask(com.omniacom.omniapp.entity.Task task) {
		Task zohoTask = new Task();
		if(task.getZohoId() != 0)
			zohoTask.setId(task.getZohoId());
		zohoTask.setName(task.getName());
		zohoTask.setEndDate(new DateFormatter("MM-dd-YYYY").print(task.getEndDate(), Locale.ENGLISH));
		zohoTask.setStartDate(new DateFormatter("MM-dd-YYYY").print(task.getStartDate(), Locale.ENGLISH));
		//zohoTask.setCompleted(task.getStatus().equals(StaticString.TASK_STATUS_COMPLETED));
		zohoTask.setCompleted(true);
		if (task.getService().getZohoId() != 0)
			zohoTask.setTasklist(convertServiceToTaskList(task.getService()));
		//zohoTask.setCompleted(task.getCompletedOn() == null ? false : true);
		//zohoTask.setPercentComplete(20);//Integer.parseInt(task.getCompletionPercentage()));
		
		//task users
		List<Owner> owners = new ArrayList<Owner>();
		for(User user : task.getUsers()) {
			Owner owner = new Owner();
			owner.setId(Long.toString(user.getZohoId()));
			owners.add(owner);
		}
		zohoTask.setOwners(owners);
		return zohoTask;
	}

}
