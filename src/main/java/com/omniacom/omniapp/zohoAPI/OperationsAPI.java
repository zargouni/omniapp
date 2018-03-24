package com.omniacom.omniapp.zohoAPI;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.OperationRepository;
import com.zoho.projects.api.MilestonesAPI;
import com.zoho.projects.model.Milestone;

@Component
public class OperationsAPI {
	
	@Autowired
	private OperationRepository operationRepo;
	
	@Autowired
	private UsersAPI usersApi;
	
	private MilestonesAPI milestonesApi;
	
	
	public Milestone pushMilestone(Operation operation, String id,User user) throws IOException {
		milestonesApi = new MilestonesAPI(usersApi.getUserAuthToken(user),
				usersApi.getPortalId(user));
		
		
		Milestone zohoResult = null;
		Milestone milestone = Converters.convertOperationToMilestone(operation);
		try {
			// TODO push to zoho + update local operation with zoho id
			zohoResult = milestonesApi.create(id, milestone);
			operation.setZohoId(zohoResult.getId());
			operationRepo.save(operation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("mahabesh");
		}
		
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

}
