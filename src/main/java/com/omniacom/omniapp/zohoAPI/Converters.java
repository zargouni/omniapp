package com.omniacom.omniapp.zohoAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.format.datetime.DateFormatter;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.User;
import com.zoho.projects.model.Bug;
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
		if (task.getZohoId() != 0)
			zohoTask.setId(task.getZohoId());
		zohoTask.setName(task.getName());
		zohoTask.setEndDate(new DateFormatter("MM-dd-YYYY").print(task.getEndDate(), Locale.ENGLISH));
		zohoTask.setStartDate(new DateFormatter("MM-dd-YYYY").print(task.getStartDate(), Locale.ENGLISH));
		if (task.getService().getZohoId() != 0)
			zohoTask.setTasklist(convertServiceToTaskList(task.getService()));
		zohoTask.setPercentComplete(50);// Integer.parseInt(task.getCompletionPercentage()));

		// task users
		List<Owner> owners = new ArrayList<Owner>();
		for (User user : task.getUsers()) {
			Owner owner = new Owner();
			owner.setId(Long.toString(user.getZohoId()));
			owners.add(owner);
		}
		zohoTask.setOwners(owners);
		return zohoTask;
	}

	public static Bug convertIssueToZohoBug(Issue issue) {
		Bug bug = new Bug();
		bug.setTitle(issue.getName());
		bug.setCreatedTime(new DateFormatter("MM-dd-YYYY").print(issue.getCreationDate(), Locale.ENGLISH));
		bug.setDescription(issue.getDescription());
		bug.setDueDate(new DateFormatter("MM-dd-YYYY").print(issue.getEndDate(), Locale.ENGLISH));
		bug.setMilestoneId(issue.getOperation().getZohoId());
		bug.setClosed(issue.getStatus().equals(StaticString.ISSUE_STATUS_CLOSED));
		bug.setProjectId(issue.getOperation().getProject().getZohoId());
		bug.setSeverityType(issue.getSeverity());
		bug.setStatusType(issue.getStatus());

		List<User> issueUsers = issue.getAssignedUsers();

		if (issueUsers.size() > 0)
			bug.setAssigneeId(Long.toString(issueUsers.get(0).getZohoId()));

		switch (issue.getStatus()) {
		case StaticString.ISSUE_STATUS_OPEN:
			bug.setStatusId(1224395000000007045L);
			break;
		case StaticString.ISSUE_STATUS_IN_PROGRESS:
			bug.setStatusId(1224395000000007048L);
			break;
		case StaticString.ISSUE_STATUS_TO_BE_TESTED:
			bug.setStatusId(1224395000000007051L);
			break;
		case StaticString.ISSUE_STATUS_CLOSED:
			bug.setStatusId(1224395000000007054L);
			break;

		}
		return bug;
	}

}
