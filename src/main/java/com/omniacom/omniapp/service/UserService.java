package com.omniacom.omniapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.UserRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private IAuthenticationFacade authenticationFacade;

	private UserRepository userRepo;
	
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	ProjectService projectService;

	@Autowired
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	public boolean zohoAuthTokenExists(User user) {
		if (user != null)
			if (user.getZohoToken() != null)
				return true;
		return false;
	}

	public Authentication getAuth() {
		Authentication auth = authenticationFacade.getAuthentication();
		return auth;
	}

	public User getSessionUser() {
		return this.findByUserName(getAuth().getName());
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepo.findOneByUserName(userName);
		if (user == null)
			throw new UsernameNotFoundException(userName);
		return new UserDetailsImpl(user);
	}

	public List<Task> findAllTasks(User user) {
		return userRepo.findAllTasks(user);
	}

	public List<Project> findAllContributedProjects(User user) {
		//return userRepo.findContributedProjects(user);
		return user.getContributedProjectList();
	}

	public User findByUserName(String userName) {
		return userRepo.findOneByUserName(userName);
	}

	public User findById(Long id) {
		return userRepo.findOne(id);
	}

	public List<Project> findAllOwnedProjects(User user) {
		return userRepo.findOwnedProjects(user);
	}

	public JSONArray findAllUsers() {
		JSONArray array = new JSONArray();
		List<User> users = (List<User>) userRepo.findAll();
		for(User user : users) {
			array.add(user.getUserName());
		}
		return array;
	}
	
	public JSONArray findAllUsersDetails() {
		JSONArray array = new JSONArray();
		List<User> users = (List<User>) userRepo.findAll();
		for(User user : users) {
			array.add(jsonUser(user));
		}
		return array;
	}
	
	public JSONObject jsonUser(User user) {
		return new JSONObject()
				.element("id", user.getId())
				.element("firstName", user.getFirstName())
				.element("lastName", user.getLastName());
			}

	public JSONArray getAllSessionUserProjects() {
		JSONArray jsonArray = new JSONArray();
		List<Project> projects = null;
		if(getSessionUser().getRole().getName().equals("ADMIN"))
			projects = (List<Project>) projectRepo.findAll();
		else 
			projects = findAllContributedProjects(getSessionUser());
		
		for (Project p : projects) {
			jsonArray.add(projectService.jsonProject(p));
		}
		return jsonArray;
	}

}
