package com.omniacom.omniapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.service.OperationService;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.service.ServiceService;
import com.omniacom.omniapp.service.TaskService;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.JsonResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	OperationService operationService;

	@Autowired
	ServiceService serviceService;

	@Autowired
	TaskService taskService;
	
	@Autowired
	UserService userService;

	@GetMapping("/project")
	public ModelAndView index(Model model) {

		if (projectService.getCurrentProject() == null)
			return new ModelAndView("404");
		return new ModelAndView("project");
	}

	@ModelAttribute
	public void addAttributes(Model model, @RequestParam("id") long projectId) {
		// set current project
		projectService.setCurrentProject(projectService.findOneById(projectId));
		model.addAttribute("selectedProject",projectService.findOneById(projectId));
		model.addAttribute("taskCount", projectService.findTaskCount(projectService.getCurrentProject()));
		model.addAttribute("completedTasksCount",
				projectService.findCompletedTasksCount(projectService.getCurrentProject()));
		model.addAttribute("onGoingTasksCount",
				projectService.findOnGoingTasksCount(projectService.getCurrentProject()));
		model.addAttribute("allServices", projectService.findAllServices(projectService.getCurrentProject()));
		model.addAttribute("ServiceTasksMap", projectService.getMapServiceTasks(projectService.getCurrentProject()));

	}
	
	@GetMapping("/get-project-operations-status")
	public @ResponseBody JSONArray getProjectOperationsStatus(@RequestParam("id") Project project) {
		return projectService.getProjectOperationsStatus(project);
	}
	
	@GetMapping("/get-project-details")
	public @ResponseBody JSONObject getprojectDetails(@RequestParam("id") long projectId) {
		Project project = projectService.findOneById(projectId);
		return projectService.jsonProject(project);
	}
	
	

	@GetMapping("/json-operations")
	public @ResponseBody JSONArray getAllOperationsJson(@RequestParam("id") long projectId) {
		return operationService.getAllOperationsJson(projectId);
	}

	@GetMapping("/get-operation-services")
	public @ResponseBody JSONArray getOperationServices(@RequestParam("id") long operationId) {
		return operationService.getOperationServices(operationId);
	}

	@GetMapping("/get-operation-details")
	public @ResponseBody JSONObject getOperationDetails(@RequestParam("id") long operationId) {
		Operation op = operationService.findOne(operationId);
		return operationService.jsonOperationFormattedDates(op);
	}
	
	@GetMapping("/get-service-details")
	public @ResponseBody JSONObject getServiceDetails(@RequestParam("id") long serviceId) {
		Service service = serviceService.findById(serviceId);
		return serviceService.jsonService(service);
	}

	@GetMapping("/json-service-tasks")
	public @ResponseBody JSONArray getAllServiceTasksJson(@RequestParam("id") long serviceId) {
		return serviceService.getAllServiceTasksJson(serviceId);
	}

	@GetMapping("/get-task-details")
	public @ResponseBody JSONObject getTaskDetails(@RequestParam("id") long taskId) {
		Task task = taskService.findOne(taskId);
		if (task != null)
			return taskService.jsonTask(task);
		return new JSONObject();
	}
	
	@PostMapping("/update-task")
	public @ResponseBody JsonResponse doUpdateTask(@RequestParam("id") long taskId, @Validated Task updatedTask,
			BindingResult result) {
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			//if (!taskService.boqNameExists(boq.getName())) {
				
				if (taskService.updateTask(taskId, updatedTask)) {

					response.setStatus("SUCCESS");
				} else {
					response.setStatus("FAIL");
				}
		//	} else if (boq.getName().equals(boqService.findOne(boqId).getName())) {
			//	if (boqService.updateBoq(boqId, boq)) {

			//		response.setStatus("SUCCESS");
			//	} else {
				//	response.setStatus("FAIL");
			//	}
			//} else {

			//	response.setStatus("FAIL");
			//	response.setResult("boq-exists");

			//}

		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}
	
	@GetMapping("/get-project-tasks-stats")
	public @ResponseBody JSONObject getProjectTasksStats(@RequestParam("id") long projectId) {
		return projectService.getProjectTaskStats(projectId);
	}
	
	@GetMapping("/get-task-parents")
	public @ResponseBody JSONObject getTaskParents(@RequestParam("id") long taskId) {
		return taskService.getTaskParents(taskId);
	}
	// @PostMapping("/get_services_all_tasks_for_current_project")
	// public @ResponseBody JSONArray
	// getAllTasksGroupedByService(@RequestParam("id") long projectId ) {
	// Map<Service, List<Task>> map =
	// projectService.getMapServiceTasks(projectService.findOneById(projectId));
	//
	// List<JSONObject> jsonArray = new ArrayList<JSONObject>();
	//
	// for (Map.Entry<Service, List<Task>> pair : map.entrySet()) {
	// JSONObject jsonService = jsonService(pair.getKey());
	// for(Task task : pair.getValue()) {
	// jsonService.accumulate("tasks", jsonTask(task));
	// }
	// jsonArray.add(jsonService);
	// //i += pair.getKey() + pair.getValue();
	// }
	// JSONArray json = JSONArray.fromObject(jsonArray);
	// return json;
	// }

	// @PostMapping("/get_tasks_by_service")
	// public @ResponseBody JSONArray getTasksByService(@RequestParam("serviceId")
	// long serviceId ) {
	// //Map<Service, List<Task>> map =
	// projectService.getMapServiceTasks(projectService.findOneById(projectId));
	// List<Task> tasks =
	// serviceService.findAllTasks(serviceService.findById(serviceId));
	// List<JSONObject> jsonList = new ArrayList<>();
	// List<JSONObject> jsonArray = new ArrayList<JSONObject>();
	//
	// for (Map.Entry<Service, List<Task>> pair : map.entrySet()) {
	// JSONObject jsonService = jsonService(pair.getKey());
	// for(Task task : pair.getValue()) {
	// jsonService.accumulate("tasks", jsonTask(task));
	// }
	// jsonArray.add(jsonService);
	// //i += pair.getKey() + pair.getValue();
	// }
	// for(Task task : tasks) {
	// JSONObject jsonTask = jsonTask(task);
	// jsonList.add(jsonTask);
	// }
	// JSONArray json = JSONArray.fromObject(jsonList);
	// return json;
	// }

	// @GetMapping("/get_tasks_by_service")
	// public @ResponseBody JSONArray getTasksByService(@RequestParam("service_id")
	// long serviceId) {
	//
	//
	//// List<Task> tasks = (List<Task>)
	// serviceService.findAllTasks(serviceService.findById(serviceId));
	//// List<JSONObject> jsonArray = new ArrayList<JSONObject>();
	//// for (Task t : tasks) {
	//// jsonArray.add(jsonTask(t));
	//// }
	//// JSONArray json = JSONArray.fromObject(jsonArray);
	// return json;
	// }

	// @GetMapping("/dashboard")
	// public ModelAndView dashboard() {
	// return new ModelAndView("fragments/project-fragments/fragment-dashboard ::
	// fragment-dashboard");
	// }
	//
	// @GetMapping("/feed")
	// public ModelAndView feed() {
	// return new ModelAndView("fragments/project-fragments/fragment-feed ::
	// fragment-feed");
	// }
	//
	// @GetMapping("/operations")
	// public ModelAndView operations() {
	// return new ModelAndView("fragments/project-fragments/fragment-operations ::
	// fragment-operations");
	// }
	//
	// @GetMapping("/tasks")
	// public ModelAndView tasks() {
	// return new ModelAndView("fragments/project-fragments/fragment-tasks ::
	// fragment-tasks");
	// }
	//
	// @GetMapping("/issues")
	// public ModelAndView issues() {
	// return new ModelAndView("fragments/project-fragments/fragment-issues ::
	// fragment-issues");
	// }
	//
	// @GetMapping("/calendar")
	// public ModelAndView calendar() {
	// return new ModelAndView("fragments/project-fragments/fragment-calendar ::
	// fragment-calendar");
	// }

	// @ModelAttribute(name = "currentProject")
	// public Project getCurrentProject() {
	// return currentProject;
	// }
	//
	// public void setCurrentProject(Project currentProject) {
	// this.currentProject = currentProject;
	// }
	
	@GetMapping("/get-all-users-in-task-details-json")
	public @ResponseBody JSONArray getAllUsersForTask(@RequestParam("id") long taskId) {
		return taskService.findAllUsersForTask(taskId);
	}

	@PostMapping("/update-task-owners")
	public @ResponseBody JsonResponse addUserToTask(@RequestParam("id") long taskId,
			@RequestParam("userId") long userId) {

		JsonResponse response = new JsonResponse();
		

		//ServiceTemplate template = stService.findOne(templateId);
		//Site site = siteService.findSite(siteId);
		User user = userService.findById(userId);
		Task task = taskService.findOne(taskId);
		if (task != null && user != null) {
			if (!taskService.addOneOwner(task, user))
				response.setStatus("FAIL");
		}
		return response;
	}
	public JSONObject jsonTask(Task task) {
		JSONObject jsonTask = new JSONObject().element("TaskId", task.getId()).element("TaskName", task.getName())
				.element("TaskStartDate", task.getStartDate().toString())
				.element("TaskEndDate", task.getEndDate().toString()).element("TaskStatus", task.getStatus())
				.element("TaskPriority", task.getPriority()).element("TaskDuration", "");
		return jsonTask;
	}

	public JSONObject jsonService(Service service) {
		JSONObject jsonService = new JSONObject().element("id", service.getId()).element("name", service.getName());
		return jsonService;
	}

}
