package com.omniacom.omniapp.zohoAPI;

import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncZohoPortal implements Job {

	@Autowired
	private ProjectsAPI projectsApi;

	@Autowired
	private OperationsAPI operationsApi;

	@Autowired
	private ServicesAPI servicesApi;

	@Autowired
	private TasksAPI tasksApi;

	@Autowired
	private IssuesAPI issuesApi;

	@Autowired
	private UsersAPI usersApi;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// Sync portal Users
		try {
			usersApi.syncPortalUsers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Sync projects to Zoho Portal
		projectsApi.pushAllProjects();

		// Sync Operations to Zoho portal
		operationsApi.pushAllMilestones();

		// Sync Services to Zoho portal
		servicesApi.pushAllTaskLists();

		// Sync Tasks to Zoho Portal
		tasksApi.pushAllTasks();

		// Update Synced Tasks to Zoho portal
		try {
			tasksApi.updateAllTasks();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Sync Issues to Zoho Portal
		issuesApi.pushAllIssues();

	}
}
