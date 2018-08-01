package com.omniacom.omniapp.zohoAPI;

import java.util.List;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.zoho.projects.model.User;

@Component
public class SyncZohoPortal implements Job {

	@Autowired
	private ProjectsAPI projectsApi;

	@Autowired
	private OperationsAPI operationsApi;
	
	@Autowired
	private ServicesAPI servicesApi;


	@Autowired
	private UsersAPI usersApi;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//Sync portal Users
				try {
					usersApi.syncPortalUsers();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		// Sync projects to Zoho Portal
		List<Project> syncedProjects = projectsApi.pushAllProjects();
		


		// Sync Operations to Zoho portal
		List<Operation> syncedOperations = operationsApi.pushAllMilestones();
		
		// Sync Services to Zoho portal
		List<Service> syncedServices = servicesApi.pushAllTaskLists();

	}
}
