package com.omniacom.omniapp.zohoAPI;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.zoho.projects.model.Milestone;

public class Converters {

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
		milestone.setStartDate(operation.getStartDate().toString());
		milestone.setEndDate(operation.getEndDate().toString());
		milestone.setFlag("internal");
		milestone.setOwnerId(661956611);
		return milestone;
	}

}
