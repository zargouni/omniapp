package com.omniacom.omniapp.zohoAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.ServiceRepository;
import com.omniacom.omniapp.repository.UserRepository;
import com.omniacom.omniapp.service.OperationService;
import com.omniacom.omniapp.service.ProjectService;
import com.zoho.projects.api.MilestonesAPI;
import com.zoho.projects.api.TasklistsAPI;
import com.zoho.projects.model.Milestone;
import com.zoho.projects.model.Tasklist;

@Component
public class ServicesAPI {

	@Autowired
	private UsersAPI usersApi;
	
	@Autowired
	private ServiceRepository serviceRepo;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private UserRepository userRepo;
	
	private TasklistsAPI taskListsApi;
	
	public Tasklist pushTaskList(Service service, String projectId,User user) throws IOException {
		taskListsApi = new TasklistsAPI(usersApi.getUserAuthToken(user),
				usersApi.getPortalId(user));
		
		
		Tasklist zohoResult = null;
		Tasklist taskList = Converters.convertServiceToTaskList(service);
		try {
			// TODO push to zoho + update local service with zoho id
			
				zohoResult = taskListsApi.create(projectId, taskList);
				service.setZohoId(zohoResult.getId());
				serviceRepo.save(service);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("mahabesh");
		}
		
		return zohoResult;
	
	}
	
	public List<Service> pushAllTaskLists() {
		// TODO Auto-generated method stub
		List<Service> unsyncedServices = null;
		List<Service> syncedServices = new ArrayList<Service>();
		List<Operation> syncedOperations = operationService.findAllSyncedOperations();
		User user = userRepo.findOne(1L);
		
		System.out.println("Running services sync ......");
		
		for(Operation op: syncedOperations) {
			unsyncedServices = operationService.findAllUnsyncedServices(op);
			System.out.println("Services to sync in Operation '"+op.getName()+"': " + unsyncedServices.size());
			
			for(Service service : unsyncedServices) {
				try {
					//System.out.println("project zoho id: "+String.valueOf(op.getProject().getZohoId()));
					this.pushTaskList(service, String.valueOf(op.getProject().getZohoId()), user);
					syncedServices.add(service);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return syncedServices;
		
	}
}
