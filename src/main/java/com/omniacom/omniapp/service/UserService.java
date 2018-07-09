package com.omniacom.omniapp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Operation;
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
	TaskService taskService;

	@Autowired
	IssueService issueService;

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
		return userRepo.findContributedProjects(user);
		// return user.getContributedProjectList();
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
		for (User user : users) {
			array.add(user.getUserName());
		}
		return array;
	}

	public JSONArray findAllUsersDetails() {
		JSONArray array = new JSONArray();
		List<User> users = (List<User>) userRepo.findAll();
		for (User user : users) {
			array.add(jsonUser(user));
		}
		return array;
	}

	public JSONObject jsonUser(User user) {
		return new JSONObject().element("id", user.getId()).element("firstName", user.getFirstName())
				.element("lastName", user.getLastName())
				.element("email", user.getEmail());
	}

	public JSONArray getAllSessionUserProjects() {
		JSONArray jsonArray = new JSONArray();
		List<Project> projects = null;
		if (getSessionUser().getRole().getName().equals("ADMIN"))
			projects = (List<Project>) projectRepo.findAll();
		else
			projects = findAllContributedProjects(getSessionUser());

		for (Project p : projects) {
			jsonArray.add(projectService.jsonProject(p));
		}
		return jsonArray;
	}

	public boolean addContributingUserToProject(User user, Project project) {
		return userRepo.addContributingUserToProject(user, project);
	}

	public JSONArray getAllUserTasksJson(long userId) {
		JSONArray array = new JSONArray();
		User user = userRepo.findOne(userId);
		List<Task> tasks = userRepo.findAllTasks(user);
		if (user != null && tasks.size() > 0) {
			for (Task t : tasks) {
				array.add(taskService.jsonTaskFormattedDates(t));
			}
		}
		return array;
	}

	public JSONArray getAllUserIssuesJson(long userId) {
		JSONArray array = new JSONArray();
		User user = userRepo.findOne(userId);
		List<Issue> issues = userRepo.findAllIssues(user);
		if (user != null && issues.size() > 0) {
			for (Issue issue : issues) {
				array.add(issueService.jsonIssueFormattedDates(issue));
			}
		}
		return array;
	}

	public Map<LocalDate, Integer> getAllUserClosedTasksFeedJson(long userId) {
		User user = userRepo.findOne(userId);
		TreeMap<LocalDate, Integer> feed = new TreeMap<LocalDate, Integer>();
		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = user.getRegisterDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		for (LocalDate date = startDate; date.isBefore(currentDate)
				|| date.isEqual(currentDate); date = date.plusDays(1)) {
			Integer count = 0;
			for (Task t : user.getClosedTasks()) {
				
				if (t.getCompletedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					count++;
				}
			}
			feed.put(date, count);
		}
		return (Map<LocalDate, Integer>) feed;
	}

	public Map<LocalDate, Integer> getAllUserClosedIssuesFeedJson(long userId) {
		User user = userRepo.findOne(userId);
		TreeMap<LocalDate, Integer> feed = new TreeMap<LocalDate, Integer>();
		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = user.getRegisterDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		for (LocalDate date = startDate; date.isBefore(currentDate)
				|| date.isEqual(currentDate); date = date.plusDays(1)) {
			Integer count = 0;
			for (Issue i : user.getClosedIssues()) {
				
				if (i.getCompletedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					count++;
				}
			}
			feed.put(date, count);
		}
		return (Map<LocalDate, Integer>) feed;
	}

	public JSONObject getUserStatsJson(long userId) {
		User user = userRepo.findOne(userId);
		JSONObject json = new JSONObject()
				.element("projects", user.getContributedProjectList().size())
				.element("tasks", user.getTasks().size())
				.element("issues", user.getIssues().size());
		return json;
	}

	public boolean updateSessionUserInfos(User user) {
		User sessionUser = getSessionUser();
		sessionUser.setFirstName(user.getFirstName());
		sessionUser.setLastName(user.getLastName());
		sessionUser.setEmail(user.getEmail());
		if(userRepo.save(sessionUser) != null)
			return true;
		return false;
	}

	public boolean updateSessionUserPassword(String currentPassword, String newPassword) {
		User user = getSessionUser();
		if(user.getPassword().equals(currentPassword)) {
			user.setPassword(newPassword);
			userRepo.save(user);
			return true;
		}
		return false;
	}



}
