package com.omniacom.omniapp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Issue;
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

	public List<Issue> findAllIssues(User user) {
		return userRepo.findAllIssues(user);
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
				.element("lastName", user.getLastName()).element("email", user.getEmail());
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
		JSONObject json = new JSONObject().element("projects", user.getContributedProjectList().size())
				.element("tasks", user.getTasks().size()).element("issues", user.getIssues().size());
		return json;
	}

	public boolean updateSessionUserInfos(User user) {
		User sessionUser = getSessionUser();
		sessionUser.setFirstName(user.getFirstName());
		sessionUser.setLastName(user.getLastName());
		sessionUser.setEmail(user.getEmail());
		if (userRepo.save(sessionUser) != null)
			return true;
		return false;
	}

	public boolean updateSessionUserPassword(String currentPassword, String newPassword) {
		User user = getSessionUser();
		if (user.getPassword().equals(currentPassword)) {
			user.setPassword(newPassword);
			userRepo.save(user);
			return true;
		}
		return false;
	}

	public JSONArray findAllOverdueItems(User user) {
		JSONArray array = new JSONArray();
		List<Task> tasks = userRepo.findAllTasks(user);
		List<Issue> issues = userRepo.findAllIssues(user);
		for (Task t : tasks) {
			if (!t.getStatus().equals(StaticString.TASK_STATUS_COMPLETED) && t.getEndDate().before(new Date())) {
				array.add(taskService.jsonTask(t).accumulate("nature", "task"));
			}
		}
		for (Issue i : issues) {
			if (!i.getStatus().equals(StaticString.ISSUE_STATUS_CLOSED) && i.getEndDate().before(new Date())) {
				array.add(issueService.jsonIssue(i).accumulate("nature", "issue"));
			}
		}
		array.sort(new Comparator<JSONObject>() {

			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				LocalDate o1Time = LocalDate.parse((String) o1.get("creationDate"));
				LocalDate o2Time = LocalDate.parse((String) o2.get("creationDate"));

				if (o1Time.isBefore(o2Time))
					return 0;
				return -1;
			}
		});
		return array;
	}

	public JSONArray findAllItemsDueToday(User user) {
		JSONArray array = new JSONArray();
		List<Task> tasks = userRepo.findAllTasks(user);
		List<Issue> issues = userRepo.findAllIssues(user);
		String todayDateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		for (Task t : tasks) {
			String endDateString = new SimpleDateFormat("dd/MM/yyyy").format(t.getEndDate());
			if (!t.getStatus().equals(StaticString.TASK_STATUS_COMPLETED) && endDateString.equals(todayDateString)) {
				array.add(taskService.jsonTask(t).accumulate("nature", "task"));
			}
		}
		for (Issue i : issues) {
			String endDateString = new SimpleDateFormat("dd/MM/yyyy").format(i.getEndDate());
			if (!i.getStatus().equals(StaticString.ISSUE_STATUS_CLOSED) && endDateString.equals(todayDateString)) {
				array.add(issueService.jsonIssue(i).accumulate("nature", "issue"));
			}
		}
		array.sort(new Comparator<JSONObject>() {

			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				LocalDate o1Time = LocalDate.parse((String) o1.get("creationDate"));
				LocalDate o2Time = LocalDate.parse((String) o2.get("creationDate"));

				if (o1Time.isBefore(o2Time))
					return 0;
				return -1;
			}
		});
		return array;
	}

	public Map<String, Integer> getUserOverviewFeedJson(long id) {
		User user = userRepo.findOne(id);
		Map<String, Integer> feed = new TreeMap<String, Integer>(new Comparator<String>() {
			public int compare(String o1, String o2) {

				SimpleDateFormat fmt = new SimpleDateFormat("MMMM", Locale.ENGLISH);
				try {
					return fmt.parse(o1).compareTo(fmt.parse(o2));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return o1.compareTo(o2);
			}
		});
		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = currentDate.minusMonths(5);// user.getRegisterDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().month;
		for (int date = startDate.getMonthValue(); date <= currentDate.getMonthValue(); date++) {
			Integer count = 0;
			for (Issue i : user.getClosedIssues()) {
				if (i.getCompletedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
						.getMonthValue() == date) {
					count++;
				}
			}

			for (Task t : user.getClosedTasks()) {
				if (t.getCompletedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
						.getMonthValue() == date) {
					count++;
				}
			}
			feed.put(LocalDate.now().withMonth(date).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " "
					+ LocalDate.now().getYear(), count);
		}
		return feed;
	}

	public JSONArray getCalendarEvents(User user) {
		JSONArray events = new JSONArray();
		List<Task> tasks = findAllTasks(user);
		List<Issue> issues = findAllIssues(user);
		for (Task t : tasks) {
			Date startDate = t.getStartDate();
			Date endDate = t.getEndDate();
			if (!t.getStatus().equals(StaticString.TASK_STATUS_COMPLETED)) {
				events.add(
						new JSONObject().element("id", t.getId()).element("type", "task").element("name", t.getName())
								.element("startDate",
										new SimpleDateFormat("dd MMMM YYYY hh:mm", Locale.ENGLISH).format(endDate))
								.element("endDate", new SimpleDateFormat("dd MMMM YYYY hh:mm", Locale.ENGLISH)
										.format(endDate.getDay() + 1)));
			}

		}
		for (Issue issue : issues) {
			if (!issue.getStatus().equals(StaticString.ISSUE_STATUS_CLOSED)) {
				events.add(new JSONObject().element("id", issue.getId()).element("type", "issue")
						.element("name", issue.getName())
						.element("startDate",
								new SimpleDateFormat("dd MMMM YYYY hh:mm", Locale.ENGLISH).format(issue.getEndDate()))
						.element("endDate",
								new SimpleDateFormat("dd MMMM YYYY hh:mm", Locale.ENGLISH).format(issue.getEndDate()))
						.element("severity", issue.getSeverity()));
			}
		}
		return events;
	}

	public JSONArray findAllUsersDetailed() {
		JSONArray array = new JSONArray();
		List<User> users = (List<User>) userRepo.findAll();
		for (User user : users) {
			array.add(new JSONObject().element("id", user.getId()).element("username", user.getUserName())
					.element("name", user.getFirstName() + " " + user.getLastName()).element("email", user.getEmail())
					.element("role", user.getRole().getName())
					.element("registerDate",
							new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(user.getRegisterDate()))
					.element("picture", user.getProfilePic() == null ? "assets/app/media/img/users/user-icon.png"
							: user.getProfilePic()));
		}
		return array;
	}

	public User findByEmail(String email) {
		return userRepo.findOneByEmail(email);
	}

	public User findByUsername(String username) {
		return userRepo.findOneByUserName(username);
	}

	public User save(User user) {
		return userRepo.save(user);
	}

	public User findByConfirmationToken(String confirmationToken) {
		return userRepo.findOneConfirmationToken(confirmationToken);
	}
}
