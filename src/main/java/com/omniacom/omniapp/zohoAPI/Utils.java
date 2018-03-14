package com.omniacom.omniapp.zohoAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.zoho.projects.api.PortalsAPI;
import com.zoho.projects.model.Milestone;
import com.zoho.projects.model.Portal;

@Component
public class Utils {
	
	public static String token;

	
	public static String getAuthToken(String email, String password) {
		URL url = null;
		try {
			url = new URL("https://accounts.zoho.com/apiauthtoken/nb/create?SCOPE=ZohoProjects/projectsapi,ZohoPC/docsapi&EMAIL_ID="
				+ email + "&PASSWORD=" + password);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = url.openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String data = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			FileCopyUtils.copy(is, bos);			
			data = new String(bos.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		token = copyAuthTokenOnly(data); 
		
		return token;
	}
	
	public static String copyAuthTokenOnly(String response) {
		int start = response.indexOf("=") + 1;
		
		return response.substring(start, response.indexOf("RESULT") - 1).trim();
	}
	
	public static String getPortalId() throws IOException{
		
		PortalsAPI portalsAPI = new PortalsAPI(token);
		List<Portal> portals = null;
		try {
			portals = portalsAPI.getPortals();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		return portals.get(0).getIdString();
	}
	
	
	}
