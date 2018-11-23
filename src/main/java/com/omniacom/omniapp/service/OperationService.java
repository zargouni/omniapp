package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Comment;
import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.LogChange;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.ServiceTemplate;
//import com.omniacom.omniapp.entity.Snag;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.TaskTemplate;
import com.omniacom.omniapp.entity.UpdateLog;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.BoqRepository;
import com.omniacom.omniapp.repository.LogChangeRepository;
import com.omniacom.omniapp.repository.OperationRepository;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.ServiceRepository;
import com.omniacom.omniapp.repository.TaskRepository;
import com.omniacom.omniapp.repository.TaskTemplateRepository;
import com.omniacom.omniapp.repository.UpdateLogRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class OperationService {

	@Autowired
	OperationRepository operationRepo;

	@Autowired
	ServiceTemplateService stService;

	@Autowired
	ServiceRepository serviceRepo;
	
	@Autowired
	IssueService issueService;

	@Autowired
	TaskRepository taskRepo;

	@Autowired
	TaskTemplateRepository taskTemplateRepo;

	@Autowired
	ProjectRepository projectRepo;

	@Autowired
	BoqRepository boqRepo;
	
	@Autowired
	LogChangeRepository changeRepo;
	
	@Autowired
	UpdateLogRepository updateLogRepo;

	@Autowired
	ServiceService serviceService;

	@Autowired
	ProjectService projectService;

	@Autowired
	SiteService siteService;

	@Autowired
	TaskService taskService;

	@Autowired
	UserService userService;

	public Operation addOperation(Operation operation) {
		operation.setCreationDate(new Date());
		return operationRepo.save(operation);
	}

	public com.omniacom.omniapp.entity.Service generateServiceFromTemplate(long operationId, long serviceTemplateId) {
		com.omniacom.omniapp.entity.Service returnService = null;
		Operation operation = operationRepo.findOne(operationId);
		ServiceTemplate template = stService.findOne(serviceTemplateId);
		if (operation != null && template != null) {
			com.omniacom.omniapp.entity.Service newService = stService.convertToService(template);
			newService.setOperation(operation);
			newService.setPriceHT(boqRepo.findServicePriceBoq(operation.getProject().getBoq(), template));
			newService.setCreationDate(new Date());
			newService.setCreatedBy(userService.getSessionUser());
			returnService = serviceRepo.save(newService);
			for (TaskTemplate t : template.getTasks()) {
				Task task = new Task();
				task.setName(t.getName());
				task.setPriority(t.getPriority());
				task.setEstimationRH(t.getEstimationHR());
				task.setEstimationTime(t.getEstimationTime());
				task.setStatus(StaticString.TASK_STATUS_ONGOING);
				task.setService(returnService);
				task.setCreationDate(new Date());
				// TODO
				Calendar calendar = Calendar.getInstance();
				calendar.set(1970, 1, 1, 0, 0);
				task.setStartDate(calendar.getTime());
				task.setEndDate(calendar.getTime());
				taskRepo.save(task);
			}

		}
		return returnService;
	}

	public JSONArray getAllOperationsJson(long projectId) {
		Project project = projectRepo.findOne(projectId);
		JSONArray jsonArray = new JSONArray();
		List<Operation> operations = project.getOperations();
		for (Operation op : operations) {
			jsonArray.add(jsonOperationFormattedDates(op));
		}
		return jsonArray;

	}

	public JSONObject jsonOperationFormattedDates(Operation op) {
		JSONObject json = new JSONObject().element("id", op.getId()).element("name", op.getName())
				.element("startDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(op.getStartDate()))
				.element("endDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(op.getEndDate()))
				.element("startDate_for_edit", new SimpleDateFormat("dd/MM/yyyy").format(op.getStartDate()))
				.element("endDate_for_edit", new SimpleDateFormat("dd/MM/yyyy").format(op.getEndDate()))
				.element("serviceCount", getOperationServiceCount(op)).element("flag", op.getFlag())
				.element("responsible_username", op.getResponsible().getUserName())
				.element("project", projectService.jsonProject(op.getProject()))
				.element("site", siteService.jsonSite(op.getSite())).element("price", getOperationPrice(op))
				.element("currency", op.getProject().getCurrency())
				.element("creationDate",
						new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH).format(op.getCreationDate()))
				.element("creationTime", new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(op.getCreationDate()))
				.element("percentage", getOperationProgress(op));
		if (op.getEndDate().before(new Date())) {
			if (getOperationStatus(op).equals("open"))
				json.accumulate("status", "Overdue");
			else if (getOperationStatus(op).equals("in_progress"))
				json.accumulate("status", "Overdue");
			else
				json.accumulate("status", "Closed");
		} else {
			if (getOperationStatus(op).equals("closed"))
				json.accumulate("status", "Closed");
			if (getOperationStatus(op).equals("open"))
				json.accumulate("status", "Open");
			if (getOperationStatus(op).equals("in_progress"))
				json.accumulate("status", "In Progress");
		}
		if (op.isDeleted())
			json.accumulate("deletionDate",
					new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH).format(op.getDeletionDate())).accumulate(
							"deletionTime", new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(op.getDeletionDate()));

		return json;
	}

	public float getOperationPrice(Operation operation) {
		List<com.omniacom.omniapp.entity.Service> services = operationRepo.findAllServices(operation);
		float price = 0;
		if (services.size() > 0)
			for (com.omniacom.omniapp.entity.Service service : services) {
				price += service.getPriceHT();
			}
		return price;
	}

	public Integer getOperationServiceCount(Operation op) {
		return operationRepo.findAllServices(op).size();
	}

	public JSONArray getOperationServices(long operationId) {
		JSONArray jsonArray = new JSONArray();
		Operation op = operationRepo.findOne(operationId);
		if (op != null && !op.getServices().isEmpty())
			for (com.omniacom.omniapp.entity.Service s : op.getServices()) {
				jsonArray.add(serviceService.jsonService(s));
			}
		return jsonArray;
	}

	public JSONArray getOperationTasks(long operationId) {
		JSONArray jsonArray = new JSONArray();
		Operation op = operationRepo.findOne(operationId);
		List<com.omniacom.omniapp.entity.Service> services = op.getServices();
		if (op != null && !services.isEmpty())
			for (com.omniacom.omniapp.entity.Service s : services) {
				List<Task> tasks = s.getTasks();
				if (!tasks.isEmpty())
					for (Task t : tasks)
						jsonArray.add(taskService.jsonTaskForGantt(t));
			}
		return jsonArray;
	}

	public Operation findOne(long operationId) {
		return operationRepo.findOne(operationId);
	}

	public String getOperationStatus(Operation op) {
		boolean open = false;
		int zeroProgressServices = 0;
		List<com.omniacom.omniapp.entity.Service> services = op.getServices();
		for (com.omniacom.omniapp.entity.Service service : services) {
			if (serviceService.getServicePercentageComplete(service).equals("0")) {
				zeroProgressServices++;
				open = true;
			}
			if (!serviceService.getServicePercentageComplete(service).equals("100")) {
				//zeroProgressServices++;
				open = true;
			}
		}
		if (open == true) {
			if (zeroProgressServices == services.size())
				return "open";
			else
				return "in_progress";
		}
		return "closed";
	}

	public String getOperationProgress(Operation op) {

		
		List<com.omniacom.omniapp.entity.Service> services = op.getServices();
		Integer percentage = 0;
		Integer fullServices = 0;
		for (com.omniacom.omniapp.entity.Service service : services) {
			percentage += Integer.valueOf(serviceService.getServicePercentageComplete(service));
			if(!service.getTasks().isEmpty()) {
				fullServices++;
			}

		}
		if(!services.isEmpty()) {
			return (percentage/fullServices)+"";
		}
		return "0";
	}

	public JSONArray getOperationComments(long operationId) {
		JSONArray array = new JSONArray();
		Operation operation = operationRepo.findOne(operationId);
		if (!operation.getComments().isEmpty()) {
			Collections.sort(operation.getComments(), new Comparator<Comment>() {
				public int compare(Comment o1, Comment o2) {
					return o2.getDate().compareTo(o1.getDate());
				}
			});
			for (Comment c : operation.getComments()) {
				array.add(jsonComment(c));
			}
		}

		return array;
	}

	public JSONObject jsonComment(Comment c) {
		return new JSONObject().element("id", c.getId())
				.element("user", c.getUser().getFirstName() + " " + c.getUser().getLastName())
				.element("user_pic",
						c.getUser().getProfilePic() == null ? "assets/app/media/img/users/user-icon.png"
								: c.getUser().getProfilePic())
				.element("user_id", c.getUser().getId())
				.element("date", new SimpleDateFormat("dd MMMM YYYY - hh:mm", Locale.ENGLISH).format(c.getDate()))
				.element("content", c.getContent());
	}

	public JSONArray getOperationSnags(long operationId) {
		JSONArray array = new JSONArray();
		Operation operation = operationRepo.findOne(operationId);
		if (!operation.getIssues().isEmpty()) {
			Collections.sort(operation.getIssues(), new Comparator<Issue>() {
				public int compare(Issue o1, Issue o2) {
					return o2.getCreationDate().compareTo(o1.getCreationDate());
				}
			});
			for (Issue s : operation.getIssues()) {
				array.add(issueService.jsonIssueFormattedDates(s));
			}
		}
		return array;
	}

//	private Object jsonSnag(Snag s) {
//		return new JSONObject().element("id", s.getId()).element("title", s.getTitle())
//				.element("severity", s.getSeverity())
//				.element("user", s.getUser().getFirstName() + " " + s.getUser().getLastName())
//				.element("user_id", s.getUser().getId())
//				.element("user_pic",
//						s.getUser().getProfilePic() == null ? "assets/app/media/img/users/user-icon.png"
//								: s.getUser().getProfilePic())
//				.element("date", new SimpleDateFormat("dd MMMM YYYY - hh:mm", Locale.ENGLISH).format(s.getDate()))
//				.element("content", s.getContent());
//	}

	public JSONObject jsonOperationGantt(Operation op) {
		JSONObject json = new JSONObject().element("id", op.getId()).element("name", op.getName())
				.element("startDate",
						new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH).format(op.getStartDate()))
				.element("endDate", new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH).format(op.getEndDate()))

				// .element("serviceCount", getOperationServiceCount(op))
				// .element("flag", op.getFlag())
				// .element("project", projectService.jsonProject(op.getProject()))
				.element("site", siteService.jsonSite(op.getSite())).element("price", getOperationPrice(op))
				.element("currency", op.getProject().getCurrency())
				// .element("creationDate",new SimpleDateFormat("dd/MM/YYYY",
				// Locale.ENGLISH).format(op.getCreationDate()))
				// .element("creationTime", new SimpleDateFormat("HH:mm",
				// Locale.ENGLISH).format(op.getCreationDate()))
				.element("percentage", getOperationProgress(op))
				// .element("services", getOperationServices(op.getId()));
				.element("tasks", getOperationTasks(op.getId()));
		// if(op.getEndDate().before(new Date())) {
		// if(getOperationStatus(op).equals("open"))
		// json.accumulate("status", "Overdue");
		// else
		// json.accumulate("status", "Closed");
		// }else {
		// if(getOperationStatus(op).equals("closed"))
		// json.accumulate("status", "Closed");
		// if(getOperationStatus(op).equals("open"))
		// json.accumulate("status", "Open");
		// }

		return json;
	}

	public List<Operation> findAllSyncedOperations() {
		return operationRepo.findAllSyncedOperations();
	}

	public List<com.omniacom.omniapp.entity.Service> findAllUnsyncedServices(Operation op) {
		return operationRepo.findAllUnsyncedServices(op);
	}

	public List<Issue> findAllUnsyncedIssues(Operation op) {
		return operationRepo.findAllUnsyncedIssues(op);
	}

	public boolean deleteOperation(long operationId) {
		if (operationRepo.exists(operationId)) {
			Operation op = operationRepo.findOne(operationId);
			op.setDeletedBy(userService.getSessionUser());
			operationRepo.save(op);
			for (com.omniacom.omniapp.entity.Service s : op.getServices()) {
				s.setDeletedBy(userService.getSessionUser());
				serviceRepo.save(s);
			}
			operationRepo.delete(op);
			return true;
		}
		return false;
	}

	public void updateOperation(Operation oldVersionOperation, Operation updatedOperation, User responsible) {
		List<LogChange> changesDone = new ArrayList<LogChange>();
		
		if (!oldVersionOperation.getName().equals(updatedOperation.getName())) {
			changesDone.add(new LogChange("name", oldVersionOperation.getName(), updatedOperation.getName()));
			oldVersionOperation.setName(updatedOperation.getName());		
		}

		if (!oldVersionOperation.getSite().equals(updatedOperation.getSite())) {
			changesDone.add(new LogChange("site", oldVersionOperation.getSite().getName(), updatedOperation.getSite().getName()));
			oldVersionOperation.setSite(updatedOperation.getSite());
		}

		String oldStartDate = new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH).format(oldVersionOperation.getStartDate());
		String newStartDate = new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH).format(updatedOperation.getStartDate());
		
		if (!oldStartDate.equals(newStartDate)) {
			changesDone.add(new LogChange("start date", oldStartDate, newStartDate));
			oldVersionOperation.setStartDate(updatedOperation.getStartDate());
		}

		String oldEndDate = new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH).format(oldVersionOperation.getEndDate());
		String newEndDate = new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH).format(updatedOperation.getEndDate());
		
		if (!oldEndDate.equals(newEndDate)) {
			changesDone.add(new LogChange("end date", oldEndDate, newEndDate));
			oldVersionOperation.setEndDate(updatedOperation.getEndDate());
		}

		if (!oldVersionOperation.getResponsible().equals(responsible)) {
			changesDone.add(new LogChange("responsible", oldVersionOperation.getResponsible().getUserName(), responsible.getUserName()));
			oldVersionOperation.setResponsible(responsible);
		}
		

		
		
		UpdateLog updateAction = new UpdateLog();
		updateAction.setActor(userService.getSessionUser());
		updateAction.setDate(new Date());
		updateAction.setOperation(oldVersionOperation);
		
		for(LogChange change : changesDone) {
			updateAction.addChange(change);//change.setUpdateLog(updateAction);
		}
		
		updateLogRepo.save(updateAction);
		
		oldVersionOperation.addUpdate(updateAction);
		

		operationRepo.save(oldVersionOperation);

		
		
//		updateAction.setChanges(changesDone);

		
		//updateLogRepo.save(updateAction);
	}

}
