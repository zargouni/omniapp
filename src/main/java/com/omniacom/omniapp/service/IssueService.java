package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.IssueRepository;
import com.omniacom.omniapp.repository.ProjectRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class IssueService {
	
	@Autowired
	IssueRepository issueRepo;
	
	@Autowired
	ProjectRepository projectRepo;
	
	public boolean addOneOwner(Issue issue, User user) {
		boolean result = issue.getAssignedUsers().add(user);
		issueRepo.save(issue);
//		if(result) {
//			notificationService.sendNotification(user, task);
//			
//			// Add contributing user to task's project
//			if (task.getService().getOperation() != null)
//				userService.addContributingUserToProject(user, task.getService().getOperation().getProject());
//			else
//				userService.addContributingUserToProject(user, task.getService().getProject());
//		}
		
		
			
		return result;
	}
	
	public JSONArray getAllIssuesJson(long projectId) {
		Project project = projectRepo.findOne(projectId);
		JSONArray jsonArray = new JSONArray();
		List<Issue> issues = projectRepo.findAllIssues(project);
		for(Issue issue : issues) {
			jsonArray.add(jsonIssueFormattedDates(issue));
		}
		return jsonArray;
		
	}
	
	public JSONObject jsonIssueFormattedDates(Issue issue) {
		JSONObject json = new JSONObject()
				.element("id", issue.getId())
				.element("name", issue.getName())
				.element("endDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(issue.getEndDate()))
				
				.element("status", issue.getStatus())
				.element("severity", issue.getSeverity())
				.element("creator", issue.getCreator().getFirstName() + ' ' + issue.getCreator().getLastName())
				.element("operationName", issue.getOperation().getName())
				.element("creationDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(issue.getCreationDate()));
		
		return json;
	}

}
