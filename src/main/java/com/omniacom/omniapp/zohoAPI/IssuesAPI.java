package com.omniacom.omniapp.zohoAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.IssueRepository;
import com.omniacom.omniapp.repository.UserRepository;
import com.omniacom.omniapp.service.OperationService;
import com.zoho.projects.api.BugsAPI;
import com.zoho.projects.exception.ProjectsException;
import com.zoho.projects.model.Bug;

@Component
public class IssuesAPI {

	private BugsAPI bugsApi;

	@Autowired
	private UsersAPI usersApi;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private IssueRepository issueRepo;

	@Autowired
	private OperationService operationService;

	public Bug pushIssue(Issue issue, String projectId, User user) throws IOException {
		bugsApi = new com.zoho.projects.api.BugsAPI(usersApi.getUserAuthToken(user), usersApi.getPortalId(user));

		Bug zohoResult = null;
		Bug zohoBug = Converters.convertIssueToZohoBug(issue);
		try {
			// TODO push to zoho + update local issue with zoho id
			zohoResult = bugsApi.create(projectId, zohoBug);
			issue.setZohoId(zohoResult.getId());
			issueRepo.save(issue);

		} catch (ProjectsException pe) {
			System.out.println("Code : " + pe.getCode() + " Message : " + pe.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return zohoResult;

	}

	public List<Issue> pushAllIssues() {
		List<Issue> unsyncedIssues = null;
		List<Issue> syncedIssues = new ArrayList<Issue>();
		List<Operation> syncedOperations = operationService.findAllSyncedOperations();
		User user = userRepo.findOne(1L);

		System.out.println("Running issues sync ......");

		for (Operation op : syncedOperations) {
			unsyncedIssues = operationService.findAllUnsyncedIssues(op);
			System.out.println("Issues to sync in Operation '" + op.getName() + "': " + unsyncedIssues.size());

			for (Issue issue : unsyncedIssues) {
				try {
					// System.out.println("PZOHOID
					// :"+String.valueOf(s.getOperation().getProject().getZohoId()));
					this.pushIssue(issue, String.valueOf(op.getProject().getZohoId()), user);
					syncedIssues.add(issue);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return syncedIssues;

	}

}
