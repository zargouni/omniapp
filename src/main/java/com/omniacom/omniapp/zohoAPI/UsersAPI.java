package com.omniacom.omniapp.zohoAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.UserRepository;
import com.zoho.projects.api.PortalsAPI;
import com.zoho.projects.api.ProjectsAPI;
import com.zoho.projects.model.Portal;
import com.zoho.projects.model.Project;
import com.zoho.projects.service.ZohoProjects;

@Service
public class UsersAPI {
	
	
	@Autowired
	private UserRepository userRepo;

		public String getUserAuthToken(User user) {
			return user.getZohoToken();
		}
		
		public String getAuthTokenFirstTime(String email, String password) {
			URL url = null;
			try {
				url = new URL("https://accounts.zoho.com/apiauthtoken/nb/create?SCOPE=ZohoProjects/projectsapi,ZohoPC/docsapi&EMAIL_ID="
					+ email + "&PASSWORD=" + password);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			InputStream is = null;
			try {
				is = url.openStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String data = null;
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				FileCopyUtils.copy(is, bos);			
				data = new String(bos.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
						
			return Utils.copyAuthTokenOnly(data);
		}
		
		public boolean updateLocalUserFromZoho(User user, String email, String password) {
			String token = getAuthTokenFirstTime(email, password);
			if (user.getZohoToken() == null) {
				if (!(token.equals("INVALID_PASSWORD") || token.equals("NO_SUCH_USER")) ) {
					user.setZohoToken(token);
					userRepo.save(user);
					return true;
				}
			}
			return false;
		}
		
		public String getPortalId(User user) throws IOException{
			
			PortalsAPI portalsAPI = new PortalsAPI(getUserAuthToken(user));
			List<Portal> portals = null;
			
			try {
				portals = portalsAPI.getPortals();
			} catch (Exception e1) {
				e1.printStackTrace();
			}	
			//System.out.println("portal id: "+portals.get(0).getIdString());
			return portals.get(0).getIdString();
		}
		
		public List<com.zoho.projects.model.User> getAllPortalUsers() throws Exception {
			ZohoProjects zoho = new ZohoProjects();
			User admin = userRepo.findOne(1L);
			zoho.initialize(admin.getZohoToken(), getPortalId(admin));
			ProjectsAPI papi = zoho.getProjectsAPI();
			List<Project> zohoProjects = papi.getProjects(null);
			List<com.zoho.projects.model.User> zohoProjectUsers = new ArrayList<com.zoho.projects.model.User>();
			List<com.zoho.projects.model.User> users = new ArrayList<com.zoho.projects.model.User>();
			for(Project p : zohoProjects) {
				zohoProjectUsers.addAll(zoho.getUsersAPI().getUsers(p.getIdString()));
			}
			
			for(com.zoho.projects.model.User user : zohoProjectUsers) {
				if(!users.contains(user))
					users.add(user);
			}
			System.out.println("users: "+users.size());
			return users;
		}
		
		public void syncPortalUsers() throws Exception {
			List<com.zoho.projects.model.User> zohoUsers = getAllPortalUsers();
			//List<User> localUsers = (List<User>) userRepo.findAll();
			for(com.zoho.projects.model.User zohoUser : zohoUsers) {
				User localUser = userRepo.findOneByEmail(zohoUser.getEmail());
				if( localUser != null && localUser.getZohoId() == 0) {
					System.out.println("zoho email: "+zohoUser.getEmail());
					localUser.setZohoId(Long.parseLong(zohoUser.getId()));
					userRepo.save(localUser);
				}
			}
		}
		

}
