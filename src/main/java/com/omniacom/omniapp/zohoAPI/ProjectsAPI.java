package com.omniacom.omniapp.zohoAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.service.ProjectService;

@Component
public class ProjectsAPI {

	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private UsersAPI usersApi;
	
	@Autowired
	private ProjectService projectService;
	
	private com.zoho.projects.api.ProjectsAPI projectsApi;
	
	
	public com.zoho.projects.model.Project pushProject(Project project) throws IOException {
		projectsApi = new com.zoho.projects.api.ProjectsAPI(usersApi.getUserAuthToken(project.getOwner()),
				usersApi.getPortalId(project.getOwner()));
		
		
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
	
	public List<Project> pushAllProjects() {
		List<Project> unsyncedProjects = null;
		List<Project> syncedProjects = new ArrayList<Project>();
		if (projectService.getUnsyncProjects() != null && projectService.getUnsyncProjects().size() > 0) {
			System.out.println("Running projects sync ......");
			unsyncedProjects = projectService.getUnsyncProjects();
			System.out.println("Projects to sync: " + unsyncedProjects.size());
			for (Project p : unsyncedProjects) {
				try {
					this.pushProject(p);
					syncedProjects.add(p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			System.out.println("Projects sync done ! ");

		}
		return syncedProjects;
		
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
