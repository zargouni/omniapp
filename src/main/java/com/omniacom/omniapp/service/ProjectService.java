package com.omniacom.omniapp.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import com.omniacom.omniapp.entity.LogChange;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.UpdateLog;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.ServiceRepository;
import com.omniacom.omniapp.repository.UpdateLogRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ProjectService {

	@Autowired
	ProjectRepository projectRepo;

	@Autowired
	ServiceRepository serviceRepo;

	@Autowired
	UpdateLogRepository updateRepo;

	@Autowired
	ServiceService serviceService;

	@Autowired
	OperationService operationService;

	@Autowired
	UserService userService;

	@Autowired
	TaskService taskService;

	private Project currentProject;

	public Project addProject(Project project) {
		project.setCreationDate(new Date());
		Project proj = projectRepo.save(project);
		com.omniacom.omniapp.entity.Service generalService = new com.omniacom.omniapp.entity.Service("General",
				project);
		generalService.setCreationDate(new Date());
		generalService.setCreatedBy(userService.getSessionUser());
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
		Map<com.omniacom.omniapp.entity.Service, List<Task>> map = new HashMap<com.omniacom.omniapp.entity.Service, List<Task>>();
		for (com.omniacom.omniapp.entity.Service s : projectRepo.findAllServices(project)) {
			map.put(s, serviceService.findAllTasks(s));

		}

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
				.element("client", project.getClient().getName()).element("client_id", project.getClient().getId())
				.element("finalClient", project.getFinalClient().getName())
				.element("finalClient_id", project.getFinalClient().getId())
				.element("owner", project.getOwner().getFirstName() + " " + project.getOwner().getLastName())
				.element("country", project.getCountry()).element("currency", project.getCurrency())
				.element("nature", project.getNature().getId())
				.element("boq_name", project.getBoq() == null ? "none" : project.getBoq().getName())
				.element("boq_id", project.getBoq() == null ? "none" : project.getBoq().getId())
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
		return projectRepo.findProjectUnplanifiedTasksCount(project);
	}

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
		// Integer taskCount = findTaskCount(project);
		Integer open = 0, inProgress = 0, toBeTested = 0, closed = 0;
		List<Issue> issues = projectRepo.findAllIssues(project);
		for (Issue issue : issues) {
			switch (issue.getStatus()) {
			case StaticString.ISSUE_STATUS_OPEN:
				open++;
				break;
			case StaticString.ISSUE_STATUS_IN_PROGRESS:
				inProgress++;
				break;
			case StaticString.ISSUE_STATUS_TO_BE_TESTED:
				toBeTested++;
				break;
			case StaticString.ISSUE_STATUS_CLOSED:
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
			json.element("open", "0").element("in_progress", "0").element("to_be_tested", "0").element("closed", "0");
		return json;
	}

	public Project save(Project project) {
		return projectRepo.save(project);

	}

	public JSONArray getProjectOperationsStatus(Project project) {
		JSONArray array = new JSONArray();
		List<Operation> operations = project.getOperations();
		for (Operation op : operations) {
			array.add(new JSONObject().element("operationId", op.getId()).element("operationName", op.getName())
					.element("siteName", op.getSite().getName()).element("latitude", op.getSite().getLatitude())
					.element("longitude", op.getSite().getLongitude())
					.element("status", operationService.getOperationStatus(op)));
		}
		return array;
	}

	public Map<LocalDate, JSONArray> getProjectFeed(Project project) {
		TreeMap<LocalDate, JSONArray> feed = new TreeMap<LocalDate, JSONArray>();
		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = project.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		JSONArray json = null;

		// Populate operation activities into map
		List<Operation> ops = projectRepo.findAllOperations(project);
		for (Operation op : ops) {

			// populate feed with operation updates
			List<UpdateLog> updates = op.getUpdates();
			if (!updates.isEmpty())
				for (UpdateLog update : updates) {
					System.out.println("iterating through updates");
					for (LocalDate date = startDate; date.isBefore(currentDate)
							|| date.isEqual(currentDate); date = date.plusDays(1)) {
						if (update.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
							JSONArray changes = new JSONArray();
							if (!update.getChanges().isEmpty())
								for (LogChange change : update.getChanges()) {
									changes.add(new JSONObject().element("field", change.getConcernedField())
											.element("old_value", change.getOldValue())
											.element("new_value", change.getNewValue()));
								}

							if (feed.containsKey(date)) {
								feed.get(date).add(new JSONObject().element("type", "operation")
										.element("activityType", "update").element("name", op.getName())
										.element("op_id", op.getId()).element("up_id", update.getId())
										.element("creationTime",
												new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(update.getDate()))
										.element("updatedBy",
												update.getActor().getFirstName() + " "
														+ update.getActor().getLastName())
										.element("user_id", update.getActor().getId()).element("changes", changes));
								feed.get(date).sort(getFeedDatesComparator());

							} else {
								json = new JSONArray();
								json.add(new JSONObject().element("type", "operation").element("activityType", "update")
										.element("name", op.getName()).element("op_id", op.getId())
										.element("up_id", update.getId())
										.element("creationTime",
												new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(update.getDate()))
										.element("updatedBy",
												update.getActor().getFirstName() + " "
														+ update.getActor().getLastName())
										.element("user_id", update.getActor().getId()).element("changes", changes));
								json.sort(getFeedDatesComparator());
								feed.put(date, json);
							}
						}

					}
				}

			// populate feed with operation creations
			for (LocalDate date = startDate; date.isBefore(currentDate)
					|| date.isEqual(currentDate); date = date.plusDays(1)) {
				if (op.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {

					if (feed.containsKey(date)) {
						feed.get(date).add(operationService.jsonOperationFormattedDates(op)
								.accumulate("type", "operation").accumulate("activityType", "creation")
								.accumulate("createdBy",
										op.getCreatedBy().getFirstName() + " " + op.getCreatedBy().getLastName())
								.accumulate("user_id", op.getCreatedBy().getId()));
						feed.get(date).sort(getFeedDatesComparator());
					} else {
						json = new JSONArray();
						json.add(operationService.jsonOperationFormattedDates(op).accumulate("type", "operation")
								.accumulate("activityType", "creation")
								.accumulate("createdBy",
										op.getCreatedBy().getFirstName() + " " + op.getCreatedBy().getLastName())
								.accumulate("user_id", op.getCreatedBy().getId()));
						json.sort(getFeedDatesComparator());
						feed.put(date, json);
					}

				}
			}

			// populate feed with operation deletions
			if (op.isDeleted()) {
				System.out.println("operation delete");
				for (LocalDate date = startDate; date.isBefore(currentDate)
						|| date.isEqual(currentDate); date = date.plusDays(1)) {
					if (op.getDeletionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
						if (feed.containsKey(date)) {
							feed.get(date).add(operationService.jsonOperationFormattedDates(op)
									.accumulate("type", "operation").accumulate("activityType", "deletion")
									.accumulate("deletedBy",
											op.getDeletedBy().getFirstName() + " " + op.getDeletedBy().getLastName())
									.accumulate("user_id", op.getDeletedBy().getId()));
							feed.get(date).sort(getFeedDatesComparator());
						} else {
							json = new JSONArray();
							json.add(operationService.jsonOperationFormattedDates(op).accumulate("type", "operation")
									.accumulate("activityType", "deletion")
									.accumulate("deletedBy",
											op.getDeletedBy().getFirstName() + " " + op.getDeletedBy().getLastName())
									.accumulate("user_id", op.getDeletedBy().getId()));
							json.sort(getFeedDatesComparator());
							feed.put(date, json);
						}
					}
				}
			}

		}

		// Populate service activities into map
		for (com.omniacom.omniapp.entity.Service service : findAllServicesEvenDeleted(project)) {

			// populate feed with service creations
			for (LocalDate date = startDate; date.isBefore(currentDate)
					|| date.isEqual(currentDate); date = date.plusDays(1)) {
				if (service.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					if (feed.containsKey(date)) {
						feed.get(date)
								.add(serviceService.jsonService(service).accumulate("type", "service")
										.accumulate("activityType", "creation")
										.accumulate("createdBy",
												service.getCreatedBy().getFirstName() + " "
														+ service.getCreatedBy().getLastName())
										.accumulate("user_id", service.getCreatedBy().getId()));
						feed.get(date).sort(getFeedDatesComparator());
					} else {
						json = new JSONArray();
						json.add(serviceService.jsonService(service).accumulate("type", "service")
								.accumulate("activityType", "creation")
								.accumulate("createdBy",
										service.getCreatedBy().getFirstName() + " "
												+ service.getCreatedBy().getLastName())
								.accumulate("user_id", service.getCreatedBy().getId()));
						json.sort(getFeedDatesComparator());
						feed.put(date, json);
					}
				}

				// populate feed with service closure
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

				// populate feed with service deletions
				if (service.isDeleted())
					if (service.getDeletionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
							.equals(date)) {
						if (feed.containsKey(date)) {
							feed.get(date)
									.add(serviceService.jsonService(service).accumulate("type", "service")
											.accumulate("activityType", "deletion")
											.accumulate("deletedBy",
													service.getDeletedBy().getFirstName() + " "
															+ service.getDeletedBy().getLastName())
											.accumulate("user_id", service.getDeletedBy().getId()));
							feed.get(date).sort(getFeedDatesComparator());
						} else {
							json = new JSONArray();
							json.add(serviceService.jsonService(service).accumulate("type", "service")
									.accumulate("activityType", "deletion")
									.accumulate("deletedBy",
											service.getDeletedBy().getFirstName() + " "
													+ service.getDeletedBy().getLastName())
									.accumulate("user_id", service.getDeletedBy().getId()));
							json.sort(getFeedDatesComparator());
							feed.put(date, json);
						}
					}

				// populate feed with service updates
				List<UpdateLog> updates = service.getUpdates();
				if (!updates.isEmpty())
					for (UpdateLog update : updates) {

						if (update.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
							JSONArray changes = new JSONArray();
							if (!update.getChanges().isEmpty())
								for (LogChange change : update.getChanges()) {
									changes.add(new JSONObject().element("field", change.getConcernedField())
											.element("old_value", change.getOldValue())
											.element("new_value", change.getNewValue()));
								}

							if (feed.containsKey(date)) {
								feed.get(date).add(new JSONObject().element("type", "service")
										.element("activityType", "update").element("name", service.getName())
										.element("service_id", service.getId()).element("up_id", update.getId())
										.element("creationTime",
												new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(update.getDate()))
										.element("updatedBy",
												update.getActor().getFirstName() + " "
														+ update.getActor().getLastName())
										.element("user_id", update.getActor().getId()).element("changes", changes));
								feed.get(date).sort(getFeedDatesComparator());

							} else {
								json = new JSONArray();
								json.add(new JSONObject().element("type", "service").element("activityType", "update")
										.element("name", service.getName()).element("service_id", service.getId())
										.element("up_id", update.getId())
										.element("creationTime",
												new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(update.getDate()))
										.element("updatedBy",
												update.getActor().getFirstName() + " "
														+ update.getActor().getLastName())
										.element("user_id", update.getActor().getId()).element("changes", changes));
								json.sort(getFeedDatesComparator());
								feed.put(date, json);
							}
						}

					}
			}

		}

		return (Map<LocalDate, JSONArray>) feed.descendingMap();
	}

	private List<com.omniacom.omniapp.entity.Service> findAllServicesEvenDeleted(Project project) {
		return projectRepo.findAllServicesEvenDeleted(project);
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
				
				//operation creations
				if (op.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					if (feed.containsKey(date))
						feed.get(date).add(op.getCreationDate());
					else {
						list = new ArrayList<Date>();
						list.add(op.getCreationDate());
						feed.put(date, list);
					}
				}
				
				//operation deletions
				if(op.isDeleted())
					if (op.getDeletionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
						if (feed.containsKey(date))
							feed.get(date).add(op.getDeletionDate());
						else {
							list = new ArrayList<Date>();
							list.add(op.getDeletionDate());
							feed.put(date, list);
						}
					}
				
				//operation updates
				for (UpdateLog update : op.getUpdates()) {
					if (update.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
						if (feed.containsKey(date))
							feed.get(date).add(update.getDate());
						else {
							list = new ArrayList<Date>();
							list.add(update.getDate());
							feed.put(date, list);
						}
					}
				}


			}

			// Populate service activities into map
			for (com.omniacom.omniapp.entity.Service service : findAllServices(project)) {
				
				//service creations
				if (service.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
					if (feed.containsKey(date))
						feed.get(date).add(service.getCreationDate());
					else {
						list = new ArrayList<Date>();
						list.add(service.getCreationDate());
						feed.put(date, list);
					}
				}
				
				//service closures
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
				
				//service deletions
				if(service.isDeleted())
					if (service.getDeletionDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate().equals(date)) {
						if (feed.containsKey(date))
							feed.get(date).add(service.getDeletionDate());
						else {
							list = new ArrayList<Date>();
							list.add(service.getDeletionDate());
							feed.put(date, list);
						}
					}
				
				//service updates
				for (UpdateLog update : service.getUpdates()) {
					if (update.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(date)) {
						if (feed.containsKey(date))
							feed.get(date).add(update.getDate());
						else {
							list = new ArrayList<Date>();
							list.add(update.getDate());
							feed.put(date, list);
						}
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
		List<Operation> operations = project.getOperations();
		for (Operation op : operations) {
			json.add(operationService.jsonOperationGantt(op));
		}
		return json;
	}

	public List<Project> getUnsyncProjects() {
		// TODO Auto-generated method stub
		return projectRepo.getUnsyncProjects();
	}

	public List<Project> getSyncProjects() {
		// TODO Auto-generated method stub
		return projectRepo.getSyncProjects();
	}

	public List<Operation> findAllUnsyncedOperations(Project project) {
		return projectRepo.findAllUnsyncedOperations(project);
	}

	public JSONArray getAllPosJson(long projectId) {
		JSONArray json = new JSONArray();
		Project project = projectRepo.findOne(projectId);
		List<com.omniacom.omniapp.entity.Service> services = projectRepo.findAllServices(project);
		Map<String, List<com.omniacom.omniapp.entity.Service>> feed = getServicesByPoNumbers(services);
		for (Map.Entry<String, List<com.omniacom.omniapp.entity.Service>> entry : feed.entrySet()) {
			String poNumber = entry.getKey();
			List<com.omniacom.omniapp.entity.Service> poServices = entry.getValue();
			Float price = 0f;
			float overallCurrentMoney = 0;
			for (com.omniacom.omniapp.entity.Service s : poServices) {
				price += s.getPriceHT();
				int globalDuration = 0;
				float serviceCurrentMoney = 0;

				// System.out.println("Service : "+s.getName());
				// calculate service's global duration in days
				for (Task task : s.getTasks()) {
					LocalDate startDate = task.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					LocalDate endDate = task.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					long elapsedDays = ChronoUnit.DAYS.between(startDate.atTime(0, 0), endDate.atTime(23, 59));
					globalDuration += elapsedDays;
				}

				// calculate progress in term of money
				for (Task task : s.getTasks()) {
					float taskCurrentMoney = 0;
					long elapsedDays = 0;
					LocalDate startDate = task.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					LocalDate endDate = task.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					elapsedDays = ChronoUnit.DAYS.between(startDate.atTime(0, 0), endDate.atTime(23, 59));
					if (globalDuration != 0)
						taskCurrentMoney = (((float) elapsedDays / globalDuration)
								* ((float) Integer.parseInt(task.getCompletionPercentage()) / 100)) * s.getPriceHT();
					// System.out.println("-----------Task: "+task.getName()+"---Duration:
					// ("+elapsedDays+"/"+ globalDuration+")---Percentage:
					// "+Integer.parseInt(task.getCompletionPercentage())+"------Money:
					// "+taskCurrentMoney+"/"+s.getPriceHT());
					serviceCurrentMoney += taskCurrentMoney;
				}

				overallCurrentMoney += serviceCurrentMoney;
				// System.out.println("Service: "+s.getName()+" has a global duration of
				// "+globalDuration+" days");

			}
			json.add(new JSONObject().element("number", poNumber).element("services", poServices.size())
					.element("price", price).element("currency", project.getCurrency())
					.element("actual_money", new DecimalFormat("##.##").format(overallCurrentMoney))
					.element("money_percentage", Math.round(overallCurrentMoney / price * 100)));
		}

		return json;
	}

	public Map<String, List<com.omniacom.omniapp.entity.Service>> getServicesByPoNumbers(
			List<com.omniacom.omniapp.entity.Service> services) {
		Map<String, List<com.omniacom.omniapp.entity.Service>> feed = new HashMap<String, List<com.omniacom.omniapp.entity.Service>>();
		List<com.omniacom.omniapp.entity.Service> list;
		for (com.omniacom.omniapp.entity.Service service : services) {
			if (!service.getName().equals("General"))
				if (service.getPoNumber() == null || service.getPoNumber().equals("NOPO")) {
					if (feed.containsKey("NOPO"))
						feed.get("NOPO").add(service);
					else {
						list = new ArrayList<com.omniacom.omniapp.entity.Service>();
						list.add(service);
						feed.put("NOPO", list);
					}
				} else {
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

	public JSONArray getAllPoServicesJson(long projectId, String poNumber) {
		List<com.omniacom.omniapp.entity.Service> services = serviceService.findAllByPoNumber(projectId, poNumber);
		JSONArray json = new JSONArray();
		for (com.omniacom.omniapp.entity.Service s : services) {
			json.add(new JSONObject().element("id", s.getId()).element("name", s.getName())
					.element("description", s.getDescription())
					.element("operation_name", s.getOperation() == null ? "none" : s.getOperation().getName())
					.element("site_name", s.getOperation() == null ? "none" : s.getOperation().getSite().getName())
					.element("po_number", s.getPoNumber()).element("price", s.getPriceHT())
					.element("currency", s.getOperation() == null ? s.getProject().getCurrency()
							: s.getOperation().getProject().getCurrency()));
		}
		return json;
	}

	public JSONArray getProjectDetailsForReports(long projectId) {
		Project project = findOneById(projectId);
		JSONArray json = new JSONArray();
		List<com.omniacom.omniapp.entity.Service> services = null;
		if (project != null) {
			services = (List<com.omniacom.omniapp.entity.Service>) findAllServices(project);
			for (com.omniacom.omniapp.entity.Service service : services) {
				if (service.getOperation() != null)
					json.add(new JSONObject().element("Activity", service.getName())
							.element("Milestone", service.getOperation().getSite().getName()).element("Localité", "")
							.element("PO Project name", project.getName()).element("Region", "")
							.element("Operation Type", service.getCategory().name())
							.element("Client", project.getClient().getName() + "-" + project.getFinalClient().getName())
							.element("Site Type", "").element("Nbre de secteurs", "")
							.element("Date de reception de la demande", "").element("Delivery PO", "")
							.element("BC n", service.getPoNumber()).element("Montant BC (HT)", service.getPriceHT())
							.element("Currency", project.getCurrency()).element("Statut", "").element("Avancement", "")
							.element("Commentaire", "").element("order to cash", "").element("Délai d'exécution", "")
							.element("OT", "").element("IF", "").element("AC", "").element("Budget de production", "")
							.element("Cout réel", "").element("DPM Batteries", "").element("Batteries Statut", "")
							.element("Baie Delta2", "").element("Planned delivery date", "")
							.element("Actual delivery date", "").element("Install Team Leader", "")
							.element("Planned instalaltion start", "").element("Actual installation Start", "")
							.element("planned installation end", "").element("Ready for swap (Installation End)", "")
							.element("Blocked Days", "").element("Swap / On air Team Leader", "")
							.element("Planned swap/on air", "").element("Acutal swap/on air", "")
							.element("QCL envoyé le", "").element("CAC 75% envoyé le", "")
							.element("CAC 75% validé le", "").element("Mail scan CAC envoyé le", "")
							.element("Mail scan CAC vali ldé le (Acceptation et facturion sur système)", "")
							.element("facuratioon sur système et Attribution de N° facture par Omniacom", "")
							.element("PAC 25% envoyé le", "").element("PAC 25% validé le", "")
							.element("CAC 25% validé le", "").element("Mail scan PAC envoyé le", "")
							.element("Mail scan PAC vali ldé le (Acceptation et facturion sur système)", "")
							.element("Validation de la facuratioon sur système et facturation réelle2", "")
							.element("PAC date limite", "").element("Type de travaux", "")
							.element("Commentaire pour client", "").element("Prix site (HT)", "")
							.element("Mob/demob (HT)", "").element("Crain (HT)", "").element("Delivery (HT)", "")
							.element("Mob/demob", "").element("Access pb", "").element("Energy pb", "")
							.element("pb de support", "").element("nb reserves critiques", "")
							.element("nb reserves Majeures", "").element("nb de reserves mineures", "")
							.element("ATP n°", "").element("Attachement 75% envoyé le", "")
							.element("Attachement 75% validé le", "").element("Column1", "").element("Prix site HT", "")
							.element("Delivery price (HT)", "").element("Extra Work (HT)", "")
							.element("Extra visit", "").element("Fourniture TGBT", "").element("Fourniture Terre", "")
							.element("Fourniture cable", "").element("Sous traitant", "INTERNE")
							.element("Achat (HT)", "").element("Description achat", "")
							.element("Prix Achat Sous Traitance (HT)", "0").element("Estimation (J/H)", "")
							.element("Country", project.getCountry()).element("BC Omniacom N", ""));
			}
		}
		return json;
	}

	public List<Project> findAll() {
		// TODO Auto-generated method stub
		return (List<Project>) projectRepo.findAll();
	}

	public boolean deleteProject(long projectId) {
		if (projectRepo.exists(projectId)) {
			projectRepo.delete(projectId);
			return true;
		}
		return false;
	}

}
