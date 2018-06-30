package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.UploadedFile;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.IssueRepository;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.UserRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class IssueService {
	
	@Autowired
	IssueRepository issueRepo;
	
	@Autowired
	ProjectRepository projectRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserService userService;
	
	@Autowired
	NotificationService notificationService;
	
	public Issue findOne(long id) {
		return issueRepo.findOne(id);
	}
	
	public boolean addOneOwner(Issue issue, User user) {
		boolean result = issue.getAssignedUsers().add(user);
		issueRepo.save(issue);
		if(result) {
			notificationService.sendIssueNotification(user, issue);
			
			// Add contributing user to task's project
				userService.addContributingUserToProject(user, issue.getProject());
			
		}
		
		
			
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
		if(issue.getAssignedUsers().isEmpty())
			json.accumulate("owners", "Unassigned");
		else {
			String str = "";
			for(User user: issue.getAssignedUsers()) {
				str += user.getFirstName()+" "+user.getLastName()+" ";
			}
			json.accumulate("owners", str);
		}
		
		return json;
	}
	
	public JSONObject jsonIssue(Issue issue) {
		return new JSONObject().element("id", issue.getId())
				.element("name", issue.getName())
				.element("endDate", new SimpleDateFormat("dd/MM/yyyy").format(issue.getEndDate()))
				.element("status", issue.getStatus())
				.element("severity", issue.getSeverity())
				.element("description", issue.getDescription())
				.element("files", findAllFiles(issue));
	}
	
	public JSONArray findAllFiles(Issue issue) {
		JSONArray array = new JSONArray();
		List<UploadedFile> files = issueRepo.findAllFiles(issue.getId());
		for(UploadedFile file : files) {
			if(!file.isDeleted())
			array.add(new JSONObject()
					.element("name", file.getName())
					.element("id", file.getId())
					.element("location", file.getLocation())
					.element("type", file.getType())
					.element("size", file.getSize())
					.element("creationDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(file.getCreationDate())));
		}
		return array;
	}
	
	public JSONObject getIssueParents(long issueId) {
		Issue issue = issueRepo.findOne(issueId);
		//com.omniacom.omniapp.entity.Service service = task.getService();
		if (issue != null)
			return new JSONObject()
					.element("operation", issue.getOperation().getName())
					.element("project", issue.getProject().getName())
					.element("project_id", issue.getProject().getId())
					.element("operation_id", issue.getOperation().getId());
		return new JSONObject();
	}
	
	public JSONArray findAllUsersForIssue(long issueId) {
		JSONArray array = new JSONArray();
		Issue issue = issueRepo.findOne(issueId);
		List<User> issueUsers = issue.getAssignedUsers();//issueRepo.findAllUsers(issue);
		List<User> users = (List<User>) userRepo.findAll();
		for (User user : users) {
			if (issueUsers.contains(user))
				array.add(userService.jsonUser(user).element("selected", true));
			else
				array.add(userService.jsonUser(user).element("selected", false));
		}
		return array;
	}
	
	public boolean updateIssue(long issueId, Issue issueCopy) {
		Issue issue = issueRepo.findOne(issueId);
		issue.getAssignedUsers().clear();
		if (issue != null) {
			issue.setName(issueCopy.getName());
			issue.setDescription(issueCopy.getDescription());
			issue.setEndDate(issueCopy.getEndDate());
			issue.setSeverity(issueCopy.getSeverity());
			issue.setStatus(issueCopy.getStatus());

			if(issueCopy.getStatus().equals(StaticString.ISSUE_STATUS_CLOSED))
				issue.setCompletedOn(new Date());
			issueRepo.save(issue);
			return true;
		}
		return false;
	}

}
