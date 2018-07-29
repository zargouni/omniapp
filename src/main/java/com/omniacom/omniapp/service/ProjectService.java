package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.ServiceRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ProjectService {

	@Autowired
	ProjectRepository projectRepo;

	@Autowired
	ServiceRepository serviceRepo;

	@Autowired
	ServiceService serviceService;

	@Autowired
	OperationService operationService;

	@Autowired
	TaskService taskService;
	
	private Project currentProject;

	public Project addProject(Project project) {
		project.setCreationDate(new Date());
		Project proj = projectRepo.save(project);
		com.omniacom.omniapp.entity.Service generalService = new com.omniacom.omniapp.entity.Service("General",
				project);
		generalService.setCreationDate(new Date());
		serviceRepo.save(generalService);
		return proj;
	}

	public Project findOneById(long id) {
		return projectRepo.findOne(id);
	}

	public List<Integer> findTaskStatusCount(Project project) {
		List<Integer> sizes = new ArrayList<Integer>();
		sizes.add(projectRepo.findAllCompletedTasks(project).size());
		sizes.add(projectRepo.findAllOnGoingTasks(project).size());
		return sizes;
	}

	public Map<com.omniacom.omniapp.entity.Service, List<Task>> getMapServiceTasks(Project project) {
		Map<com.omniacom.omniapp.entity.Service, List<Task>> map = new TreeMap<>();
		for (com.omniacom.omniapp.entity.Service s : projectRepo.findAllServices(project))
			map.put(s, serviceService.findAllTasks(s));
		return map;
	}

	public JSONArray getAllOperationsJson(long projectId) {
		JSONArray array = new JSONArray();
		Project project = projectRepo.findOne(projectId);
		if (project != null) {
			for (Operation op : project.getOperations()) {
				array.add(new JSONObject().element("id", op.getId()).element("name", op.getName()));
			}
		}
		return array;
	}

	public Integer findCompletedTasksCount(Project project) {
		return projectRepo.findAllCompletedTasks(project).size();
	}

	public Integer findOnGoingTasksCount(Project project) {
		return projectRepo.findAllOnGoingTasks(project).size();
	}

	public Integer findTaskCount(Project project) {
		return projectRepo.findAllCompletedTasks(project).size() + projectRepo.findAllOnGoingTasks(project).size();
	}

	public Iterable<com.omniacom.omniapp.entity.Service> findAllServices(Project selectedProject) {
		// TODO Auto-generated method stub
		return projectRepo.findAllServices(selectedProject);
	}

	public List<BillOfQuantities> findAllBoqs(Project project) {
		return projectRepo.findAllBoqs(project);
	}

	/**
	 * @return the currentProject
	 */
	public Project getCurrentProject() {
		return currentProject;
	}

	/**
	 * @param currentProject
	 *            the currentProject to set
	 */
	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}

	public JSONObject jsonProject(Project project) {
		JSONObject json = new JSONObject().element("id", project.getId()).element("name", project.getName())
				.element("client", project.getClient().getName())
				.element("owner", project.getOwner().getFirstName() + " " + project.getOwner().getLastName())
				.element("country", project.getCountry()).element("currency", project.getCurrency())
				.element("percentage", getProjectProgress(project))
				.element("unassignedTasksCount", getProjectUnassignedTasksCount(project))
				.element("unplanifiedTasksCount", getProjectUnplanifiedTasksCount(project))
				.element("overdueTasksCount", getProjectOverdueTasksCount(project))
				.element("tasksCount", findTaskCount(project))
				.element("creationDate",
						new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(project.getCreationDate()))
				.element("issuesCount", getProjectIssuesCount(project))
				.element("unassignedIssuesCount", getProjectUnassignedIssuesCount(project))
				.element("overdueIssuesCount", getProjectOverdueIssuesCount(project));
		return json;
	}

	private Integer getProjectUnplanifiedTasksCount(Project project) {
		return projectRepo.findProjectUnplanifiedTasksCount(project);	}

	private Integer getProjectIssuesCount(Project project) {
		return projectRepo.findAllIssues(project).size();
	}
	
	public Integer getProjectUnassignedIssuesCount(Project project) {
		return projectRepo.findProjectUnassignedIssuesCount(project);
	}
	
	public Integer getProjectOverdueIssuesCount(Project project) {
		return projectRepo.findProjectOverdueIssuesCount(project);
	}

	public Integer getProjectUnassignedTasksCount(Project project) {
		return projectRepo.findProjectUnassignedTasksCount(project);
	}

	public Integer getProjectOverdueTasksCount(Project project) {
		return projectRepo.findProjectOverdueTasksCount(project);
	}

	public String getProjectProgress(Project project) {
		Integer taskCount = findTaskCount(project);
		if (taskCount != 0)
			return (findCompletedTasksCount(project) * 100 / taskCount) + "%";
		return "0%";
	}

	public JSONObject getProjectTaskStats(long projectId) {
		JSONObject json = new JSONObject();
		Project project = projectRepo.findOne(projectId);
		Integer taskCount = findTaskCount(project);

		if (taskCount != 0) {
			json.element("totalTasks", taskCount)
					.element("ongoing", (findOnGoingTasksCount(project) * 100 / taskCount) + "%")
					.element("completed", (findCompletedTasksCount(project) * 100 / taskCount) + "%");
		} else
			json.element("totalTasks", taskCount).element("ongoing", "None").element("completed", "None");

		return json;
	}
	
	public JSONObject getProjectIssuesStats(long projectId) {
		JSONObject json = new JSONObject();
		Project project = projectRepo.findOne(projectId);
		//Integer taskCount = findTaskCount(project);
		Integer open = 0 , inProgress = 0, toBeTested = 0, closed = 0;
		List<Issue> issues = projectRepo.findAllIssues(project);
		for(Issue issue : issues) {
			switch(issue.getStatus()) {
			   case StaticString.ISSUE_STATUS_OPEN:
				   open++;
				   break;
			   case StaticString.ISSUE_STATUS_IN_PROGRESS :
				   inProgress++;
				   break; 
			   case StaticString.ISSUE_STATUS_TO_BE_TESTED :
				   toBeTested++;
				   break;
			   case StaticString.ISSUE_STATUS_CLOSED :
				   closed++;
				   break;
			}
		}
		if (issues.size() != 0) {
				json.element("open", (open * 100 / issues.size()) + "%")
					.element("in_progress", (inProgress * 100 / issues.size()) + "%")
					.element("to_be_tested", (toBeTested * 100 / issues.size()) + "%")
					.element("closed", (closed * 100 / issues.size()) + "%");
		} else
			json.element("open", "0")
			.element("in_progress", "0")
			.element("to_be_tested", "0")
			.element("closed", "0");
		return json;
	}

	public Project save(Project project) {
		return projectRepo.save(project);

	}

	public JSONArray getProjectOperationsStatus(Project project) {
		JSONArray array = new JSONArray();
		List<Operation> operations = projectRepo.findAllOperations(project);
		for (Operation op : operations) {
			array.add(new JSONObject().element("operationId", op.getId()).element("operationName", op.getName())
					.element("siteName", op.getSite().getName()).element("latitude", op.getSite().getLatitude())
					.element("longitude", op.getSite().getLongitude())
					.element("status", operationService.getOperationStatus(op)));
		}
		return array;
	}

	public Map<LocalDate, JSONArray> getProjectFeed(Project project) {
		// JSONArray json = new JSONArray();
		// Set<Object> activities = null;
		TreeMap<LocalDate, JSONArray> feed = new TreeMap<LocalDate, JSONArray>();
		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = project.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		JSONArray json = null;
		for (LocalDate date = startDate; date.isBefore(currentDate)
				|| date.isEqual(currentDate); date = date.plusDays(1)) {
			// Populate operation activities into map
			for (Operation op : project.getOperations()) {
				if (op.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					if (feed.containsKey(date)) {
						feed.get(date)
								.add(operationService.jsonOperationFormattedDates(op).accumulate("type", "operation"));
						feed.get(date).sort(getFeedDatesComparator());
					} else {
						json = new JSONArray();
						// activities = new TreeSet<>(getFeedDatesComparator());
						// activities.add(op);
						json.add(operationService.jsonOperationFormattedDates(op).accumulate("type", "operation"));
						json.sort(getFeedDatesComparator());
						feed.put(date, json);
					}
				}

			}

			// Populate service activities into map
			for (com.omniacom.omniapp.entity.Service service : findAllServices(project)) {
				if (service.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					if (feed.containsKey(date)) {
						feed.get(date).add(serviceService.jsonService(service).accumulate("type", "service")
								.accumulate("activityType", "creation"));
						feed.get(date).sort(getFeedDatesComparator());
					} else {
						json = new JSONArray();
						json.add(serviceService.jsonService(service).accumulate("type", "service")
								.accumulate("activityType", "creation"));
						json.sort(getFeedDatesComparator());
						feed.put(date, json);
					}
				}
				if (serviceService.getServiceClosedDate(service) != null)
					if (serviceService.getServiceClosedDate(service).toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate().equals(date)) {
						if (feed.containsKey(date)) {
							feed.get(date).add(serviceService.jsonService(service).accumulate("type", "service")
									.accumulate("activityType", "closed"));
							feed.get(date).sort(getFeedDatesComparator());
						}

						else {
							json = new JSONArray();
							json.add(serviceService.jsonService(service).accumulate("type", "service")
									.accumulate("activityType", "closed"));
							json.sort(getFeedDatesComparator());
							feed.put(date, json);
						}
					}

			}

		}
		return (Map<LocalDate, JSONArray>) feed.descendingMap();
	}

	private Comparator<JSONObject> getFeedDatesComparator() {
		return new Comparator<JSONObject>() {

			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				LocalDateTime o1Time = LocalDate.now().atTime(LocalTime.parse((String) o1.get("creationTime")));
				LocalDateTime o2Time = LocalDate.now().atTime(LocalTime.parse((String) o2.get("creationTime")));

				if (o1Time.isBefore(o2Time))
					return 0;
				return -1;
			}
		};

	}

	public Map<LocalDate, List<Date>> getRawProjectFeed(Project project) {

		TreeMap<LocalDate, List<Date>> feed = new TreeMap<LocalDate, List<Date>>();
		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = project.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		List<Date> list = null;
		for (LocalDate date = startDate; date.isBefore(currentDate)
				|| date.isEqual(currentDate); date = date.plusDays(1)) {
			// Populate operation activities into map
			for (Operation op : project.getOperations()) {
				if (op.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					if (feed.containsKey(date))
						feed.get(date).add(op.getCreationDate());
					else {
						list = new ArrayList<Date>();
						list.add(op.getCreationDate());
						feed.put(date, list);
					}
				}

			}

			// Populate service activities into map
			for (com.omniacom.omniapp.entity.Service service : findAllServices(project)) {
				if (service.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					if (feed.containsKey(date))
						feed.get(date).add(service.getCreationDate());
					else {
						list = new ArrayList<Date>();
						list.add(service.getCreationDate());
						feed.put(date, list);
					}
				}
				if (serviceService.getServiceClosedDate(service) != null)
					if (serviceService.getServiceClosedDate(service).toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate().equals(date)) {
						if (feed.containsKey(date))
							feed.get(date).add(service.getCreationDate());
						else {
							list = new ArrayList<Date>();
							list.add(service.getCreationDate());
							feed.put(date, list);
						}
					}

			}

		}
		return (Map<LocalDate, List<Date>>) feed.descendingMap();
	}

	public JSONArray getProjectEvents(Project project) {
		// TODO Auto-generated method stub
		JSONArray events = new JSONArray();
		List<com.omniacom.omniapp.entity.Service> services = (List<com.omniacom.omniapp.entity.Service>) findAllServices(
				project);
		List<Operation> operations = project.getOperations();
		for (Operation op : operations) {
			Date startDate = op.getStartDate();
			Date endDate = op.getEndDate();
			// System.out.println("startDate: "+startDate);
			// System.out.println("endDate: "+endDate);
			events.add(new JSONObject().element("id", op.getId()).element("type", "operation")
					.element("name", op.getName())
					.element("startDate", new SimpleDateFormat("dd MMMM YYYY hh:mm", Locale.ENGLISH).format(endDate))
					.element("endDate",
							new SimpleDateFormat("dd MMMM YYYY hh:mm", Locale.ENGLISH).format(endDate.getDay() + 1)));

		}

		for (com.omniacom.omniapp.entity.Service service : services) {
			JSONObject json = new JSONObject().element("id", service.getId()).element("type", "service").element("name",
					service.getName());
			// if(serviceService.getServiceDates(service).size() != 0) {
			json.element("startDate", new SimpleDateFormat("dd MMMM YYYY hh:mm", Locale.ENGLISH)
					.format(serviceService.getServiceDates(service)));
			json.element("endDate", new SimpleDateFormat("dd MMMM YYYY hh:mm", Locale.ENGLISH)
					.format(serviceService.getServiceDates(service).getDay() + 1));
			// }else {
			// json.element("startDate", new Date());
			// json.element("endDate", new Date());
			// }
			events.add(json);

		}
		return events;
	}
	
	public JSONArray getGanttContent(Project project) {
		JSONArray json = new JSONArray();
		List<Operation> operations = projectRepo.findAllOperations(project);
		for(Operation op : operations) {
			json.add(operationService.jsonOperationGantt(op));
		}
		return json;
	}

	public List<Project> getUnsyncProjects() {
		// TODO Auto-generated method stub
		return projectRepo.getUnsyncProjects();
	}

	public List<Operation> findAllUnsyncedOperations(Project project) {
		return projectRepo.findAllUnsyncedOperations(project);
	}

	public JSONArray getAllPosJson(long projectId) {
		JSONArray json = new JSONArray();
		Project project = projectRepo.findOne(projectId);
		List<com.omniacom.omniapp.entity.Service> services = projectRepo.findAllServices(project);
		Map<String,List<com.omniacom.omniapp.entity.Service>> feed = getServicesByPoNumbers(services);
		for (Map.Entry<String, List<com.omniacom.omniapp.entity.Service>> entry : feed.entrySet()) {
		    String poNumber = entry.getKey();
		    List<com.omniacom.omniapp.entity.Service> poServices = entry.getValue();
		    Float price = 0f;
		    for(com.omniacom.omniapp.entity.Service s : poServices) {
		    	price += s.getPriceHT();
		    }
		    json.add(new JSONObject()
		    		.element("number", poNumber)
		    		.element("services", poServices.size())
		    		.element("price", price));
		}

		return json;
	}
	
	public Map<String,List<com.omniacom.omniapp.entity.Service>> getServicesByPoNumbers(List<com.omniacom.omniapp.entity.Service> services)
	{
		Map<String,List<com.omniacom.omniapp.entity.Service>> feed = new HashMap<String,List<com.omniacom.omniapp.entity.Service>>();
		List<com.omniacom.omniapp.entity.Service> list;
		for (com.omniacom.omniapp.entity.Service service : services) {
			if (service.getPoNumber() == null || service.getPoNumber().equals("NOPO")) {
				if (feed.containsKey("NOPO"))
					feed.get("NOPO").add(service);
				else {
					list = new ArrayList<com.omniacom.omniapp.entity.Service>();
					list.add(service);
					feed.put("NOPO", list);
				}
			}else {
				if (feed.containsKey(service.getPoNumber().toUpperCase()))
					feed.get(service.getPoNumber().toUpperCase()).add(service);
				else {
					list = new ArrayList<com.omniacom.omniapp.entity.Service>();
					list.add(service);
					feed.put(service.getPoNumber().toUpperCase(), list);
				}
			}
			

		}
		return feed;
	}

}
