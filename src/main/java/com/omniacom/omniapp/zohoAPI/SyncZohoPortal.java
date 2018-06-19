package com.omniacom.omniapp.zohoAPI;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;

@Component
public class SyncZohoPortal implements Job {

	@Autowired
	private ProjectsAPI projectsApi;

	@Autowired
	private OperationsAPI operationsApi;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// Sync projects to Zoho Portal
		List<Project> syncedProjects = projectsApi.pushAllProjects();

		// Sync Operations to Zoho portal
		List<Operation> syncedOperations = operationsApi.pushAllMilestones(syncedProjects);

	}
}
