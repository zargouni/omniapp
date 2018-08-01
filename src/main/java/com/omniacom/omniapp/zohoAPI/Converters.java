package com.omniacom.omniapp.zohoAPI;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.zoho.projects.model.Milestone;
import com.zoho.projects.model.Tasklist;

public class Converters {

	@Autowired
	private UsersAPI usersApi;

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
		taskList.setMilestone(convertOperationToMilestone(service.getOperation()));
		taskList.setCreatedTime(new DateFormatter("MM-dd-YYYY").print(service.getCreationDate(), Locale.ENGLISH));
		taskList.setFlag("internal");
		return taskList;
	}

}
