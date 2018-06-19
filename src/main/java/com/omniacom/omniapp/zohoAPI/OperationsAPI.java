package com.omniacom.omniapp.zohoAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mysql.fabric.xmlrpc.base.Array;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.OperationRepository;
import com.omniacom.omniapp.service.ProjectService;
import com.zoho.projects.api.MilestonesAPI;
import com.zoho.projects.model.Milestone;

@Component
public class OperationsAPI {
	
	@Autowired
	private OperationRepository operationRepo;
	
	@Autowired
	private UsersAPI usersApi;
	
	@Autowired
	private ProjectService projectService;
	
	private MilestonesAPI milestonesApi;
	
	
	public Milestone pushMilestone(Operation operation, String projectId,User user) throws IOException {
		milestonesApi = new MilestonesAPI(usersApi.getUserAuthToken(user),
				usersApi.getPortalId(user));
		
		
		Milestone zohoResult = null;
//		Milestone milestone = Converters.convertOperationToMilestone(operation);
//		try {
//			// TODO push to zoho + update local operation with zoho id
//			zohoResult = milestonesApi.create(projectId, milestone);
//			operation.setZohoId(zohoResult.getId());
//			operationRepo.save(operation);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("mahabesh");
//		}
		
		return zohoResult;
	
	}	
	
	
	public boolean deleteMilestone(Milestone milestone) {
		try {
			//milestonesApi.delete(milestone., milestoneId);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


	public List<Operation> pushAllMilestones(List<Project> syncedProjects) {
		// TODO Auto-generated method stub
		List<Operation> unsyncedOperations = null;
		List<Operation> syncedOperations = new ArrayList<Operation>();
		for(Project p: syncedProjects) {
			unsyncedOperations = projectService.findAllUnsyncedOperations(p);
			for(Operation op : unsyncedOperations) {
				try {
					System.out.println("project zoho id: "+String.valueOf(op.getProject().getZohoId()));
					pushMilestone(op, String.valueOf(op.getProject().getZohoId()), op.getProject().getOwner());
					syncedOperations.add(op);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return syncedOperations;
		
	}

}
