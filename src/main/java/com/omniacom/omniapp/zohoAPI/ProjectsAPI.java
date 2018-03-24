package com.omniacom.omniapp.zohoAPI;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.ProjectRepository;

@Component
public class ProjectsAPI {

	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private UsersAPI usersApi;
	
	private com.zoho.projects.api.ProjectsAPI projectsApi;
	
	
	public com.zoho.projects.model.Project pushProject(Project project,User user) throws IOException {
		projectsApi = new com.zoho.projects.api.ProjectsAPI(usersApi.getUserAuthToken(user),
				usersApi.getPortalId(user));
		
		
		com.zoho.projects.model.Project zohoResult = null;
		com.zoho.projects.model.Project zohoProject = Converters.convertLocalProjectToZoho(project);
		try {
			// TODO push to zoho + update local project with zoho id
			zohoResult = projectsApi.create(zohoProject);
			project.setZohoId(zohoResult.getId());
			projectRepo.save(project);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return zohoResult;
	
	}	
	
	public boolean deleteProject(com.zoho.projects.model.Project project) {
		try {
			projectsApi.delete(project.getIdString());
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
