package com.omniacom.omniapp.zohoAPI;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.zoho.projects.model.Milestone;

public class Converters {

	@Autowired
	private UsersAPI usersApi;
	public static com.zoho.projects.model.Project convertLocalProjectToZoho(Project project) {

		com.zoho.projects.model.Project zohoProject = new com.zoho.projects.model.Project();

		zohoProject.setName(project.getName());
		zohoProject.setDescription(project.getDescription());
		zohoProject.setCreatedDate(project.getCreationDate().toString());
		return zohoProject;
	}
	
	public static Milestone convertOperationToMilestone(Operation operation) {
		Milestone milestone = new Milestone();
		milestone.setName(operation.getName());
		milestone.setStartDate(new DateFormatter("MM-dd-YYYY").print(operation.getStartDate(), Locale.ENGLISH));
		
		milestone.setEndDate(new DateFormatter("MM-dd-YYYY").print(operation.getEndDate(), Locale.ENGLISH));
		
		milestone.setFlag("internal");
		milestone.setOwnerId(661956611);
		return milestone;
	}

}
