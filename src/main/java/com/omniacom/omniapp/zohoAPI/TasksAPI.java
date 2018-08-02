package com.omniacom.omniapp.zohoAPI;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.TaskRepository;
import com.omniacom.omniapp.repository.UserRepository;
import com.omniacom.omniapp.service.ServiceService;
import com.omniacom.omniapp.service.TaskService;
import com.zoho.projects.exception.ProjectsException;

@Component
public class TasksAPI {

	private com.zoho.projects.api.TasksAPI tasksApi;

	@Autowired
	private UsersAPI usersApi;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TaskRepository taskRepo;

	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private TaskService taskService;

	public com.zoho.projects.model.Task pushTask(Task task, String projectId, User user) throws IOException {
		tasksApi = new com.zoho.projects.api.TasksAPI(usersApi.getUserAuthToken(user), usersApi.getPortalId(user));

		com.zoho.projects.model.Task zohoResult = null;
		com.zoho.projects.model.Task zohoTask = Converters.convertTaskToZohoTask(task);
		try {
			// TODO push to zoho + update local task with zoho id
			zohoResult = tasksApi.create(projectId, zohoTask);
			task.setZohoId(zohoResult.getId());
			taskRepo.save(task);

		} catch (ProjectsException pe) {
			System.out.println("Code : "+ pe.getCode() +" Message : "+ pe.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return zohoResult;

	}

	public List<Task> pushAllTasks() {
		List<Task> unsyncedTasks = null;
		List<Task> syncedTasks = new ArrayList<Task>();
		List<Service> syncedServices = serviceService.findAllSyncedServices();
		User user = userRepo.findOne(1L);

		System.out.println("Running tasks sync ......");

		for (Service s : syncedServices) {
			unsyncedTasks = serviceService.findAllUnsyncedTasks(s);
			System.out.println("Tasks to sync in TaskList '" + s.getName() + "': " + unsyncedTasks.size());

			for (Task task : unsyncedTasks) {
				try {
					 //System.out.println("PZOHOID :"+String.valueOf(s.getOperation().getProject().getZohoId()));
					this.pushTask(task, String.valueOf(s.getOperation().getProject().getZohoId()), user);
					syncedTasks.add(task);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return syncedTasks;

	}
	
    private static HttpURLConnection con;

	public void updateAllTasks() throws IOException {
		List<Task> syncedTasks = taskService.findAllSyncedTasks();
		User user = userRepo.findOne(1L);
		tasksApi = new com.zoho.projects.api.TasksAPI(usersApi.getUserAuthToken(user), usersApi.getPortalId(user));

		for(Task task : syncedTasks) {
			try {
				tasksApi.update(String.valueOf(task.getService().getOperation().getProject().getZohoId()), Converters.convertTaskToZohoTask(task));
			} catch (ProjectsException pe) {
				System.out.println("Code : "+ pe.getCode() +" Message : "+ pe.getMessage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			String url = "https://projectsapi.zoho.com/restapi/portal/"
//					+usersApi.getPortalId(user)+"/projects/"
//					+String.valueOf(task.getService().getOperation().getProject().getZohoId())
//					+"/tasks/"+task.getZohoId()+"/";
//	        String urlParameters = "percent_complete="+task.getCompletionPercentage();
//	        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
//
//	        try {
//
//	            URL myurl = new URL(url);
//	            con = (HttpURLConnection) myurl.openConnection();
//
//	            con.setDoOutput(true);
//	            con.setRequestMethod("POST");
//	            con.setRequestProperty("User-Agent", "Java client");
//	            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
//	            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
//	                wr.write(postData);
//	            }
//
//	            StringBuilder content;
//
//	            try (BufferedReader in = new BufferedReader(
//	                    new InputStreamReader(con.getInputStream()))) {
//
//	                String line;
//	                content = new StringBuilder();
//
//	                while ((line = in.readLine()) != null) {
//	                    content.append(line);
//	                    content.append(System.lineSeparator());
//	                }
//	            }
//
//	            System.out.println(content.toString());
//
//	        } finally {
//	            
//	            con.disconnect();
//	        }
		}
		
	
		
	}
}
