package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.BillOfQuantities;
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
				.element("overdueTasksCount", getProjectOverdueTasksCount(project))
				.element("tasksCount", findTaskCount(project))
				.element("creationDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(project.getCreationDate()));
		return json;
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
		//JSONArray json = new JSONArray();
		//Set<Object> activities = null;
		TreeMap<LocalDate, JSONArray> feed = new TreeMap<LocalDate, JSONArray>();
		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = project.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		JSONArray json = null;
		for (LocalDate date = startDate; date.isBefore(currentDate) || date.isEqual(currentDate) ; date = date.plusDays(1)) {
			// Populate operation activities into map
			for (Operation op : project.getOperations()) {
				if (op.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					if (feed.containsKey(date)) {
						feed.get(date)
								.add(operationService.jsonOperationFormattedDates(op).accumulate("type", "operation"));
						feed.get(date).sort(getFeedDatesComparator());
					}else {
						json = new JSONArray();
						//activities = new TreeSet<>(getFeedDatesComparator());
						//activities.add(op);
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
					}else {
						json = new JSONArray();
						json.add(serviceService.jsonService(service).accumulate("type", "service")
								.accumulate("activityType", "creation"));
						json.sort(getFeedDatesComparator());
						feed.put(date, json);
					}
				}
				if(serviceService.getServiceClosedDate(service) != null)
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
	
private Comparator<JSONObject> getFeedDatesComparator(){
	return new Comparator<JSONObject>() {

		@Override
		public int compare(JSONObject o1, JSONObject o2) {
			LocalDateTime o1Time = LocalDate.now().atTime(LocalTime.parse((String) o1.get("creationTime")));
			LocalDateTime o2Time = LocalDate.now().atTime(LocalTime.parse((String) o2.get("creationTime")));

			if(o1Time.isBefore(o2Time))
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
		for (LocalDate date = startDate; date.isBefore(currentDate) || date.isEqual(currentDate) ; date = date.plusDays(1)) {
			// Populate operation activities into map
			for (Operation op : project.getOperations()) {
				if (op.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					if (feed.containsKey(date))
						feed.get(date)
								.add(op.getCreationDate());
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
				if(serviceService.getServiceClosedDate(service) != null)
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

}
