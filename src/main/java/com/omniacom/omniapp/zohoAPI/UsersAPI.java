package com.omniacom.omniapp.zohoAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.UserRepository;
import com.zoho.projects.api.PortalsAPI;
import com.zoho.projects.model.Portal;

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
			return portals.get(0).getIdString();
		}

}
