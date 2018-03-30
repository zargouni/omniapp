package com.omniacom.omniapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.service.ClientService;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.service.TaskService;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.JsonResponse;
import com.omniacom.omniapp.validator.ProjectValidator;
import com.omniacom.omniapp.validator.TaskValidator;
import com.omniacom.omniapp.zohoAPI.ProjectsAPI;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@ControllerAdvice(annotations = RestController.class)
@RequestMapping("/")
public class MajorControllerAdvice extends ResponseEntityExceptionHandler {

	private String TASK_STATUS_ONGOING = StaticString.TASK_STATUS_ONGOING;
	private String TASK_STATUS_COMPLETED = StaticString.TASK_STATUS_COMPLETED;

	@Autowired
	private ProjectValidator projectValidator;
	
	@Autowired
	private TaskValidator taskValidator;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectsAPI projectsApi;

	@Autowired
	private TaskService taskService;

	@ModelAttribute
	public void addAtributes(Model model) {
		model.addAttribute("TASK_STATUS_ONGOING", this.TASK_STATUS_ONGOING);
		model.addAttribute("TASK_STATUS_COMPLETED", this.TASK_STATUS_COMPLETED);
		model.addAttribute("project", new Project());
		model.addAttribute("clients", clientService.findAllClients());
		model.addAttribute("task", new Task());
		model.addAttribute("selectedProject", new Project());
		model.addAttribute("sessionUser", userService.getSessionUser());

	}

	// private List<Service> selectedProjectServices;

	@GetMapping("/set-select-owned-projects")
	public @ResponseBody JSONArray setSelectOwnedProjects() {
		List<Project> ownedProjects = (List<Project>) userService.findAllOwnedProjects(userService.getSessionUser());
		List<JSONObject> jsonArray = new ArrayList<JSONObject>();
		for (Project p : ownedProjects) {
			jsonArray.add(jsonProject(p));
		}
		JSONArray json = JSONArray.fromObject(jsonArray);
		return json;
	}

	@GetMapping("/set-selected-project-services")
	public @ResponseBody JSONArray setSelectedProjectServices(@RequestParam("projectId") long projectId) {
		List<Service> selectedProjectServices = (List<Service>) projectService
				.findAllServices(projectService.findOneById(projectId));
		List<JSONObject> jsonArray = new ArrayList<JSONObject>();
		for (Service s : selectedProjectServices) {
			jsonArray.add(jsonService(s));
		}
		JSONArray json = JSONArray.fromObject(jsonArray);
		return json;
	}

	@GetMapping("/")
	public String index(Model model) {
		if (userService.getSessionUser().getZohoToken() == null)
			return "redirect:/zoho";

		return "redirect:/home";
	}

	@InitBinder("project")
	public void setupBinder(WebDataBinder binder) {
	    binder.addValidators(projectValidator);
	}
	
	@InitBinder("task")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(taskValidator);
	}

	@PostMapping("/add-project")
	public @ResponseBody JsonResponse addProject(@ModelAttribute("project") @Validated Project project,
			BindingResult result) throws IOException {

		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			projectService.addProject(project);

			response.setStatus("SUCCESS");
			// projectsApi.pushProject(project, getSessionUser());
			// response.setResult(userList);
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@PostMapping("/add-task")
	public @ResponseBody JsonResponse addTask(@Validated @ModelAttribute("task") Task task, BindingResult result)
			throws IOException {

		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			taskService.addTask(task);
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	public JSONObject jsonService(Service service) {
		JSONObject jsonService = new JSONObject().element("id", service.getId()).element("name", service.getName());
		return jsonService;
	}

	public JSONObject jsonProject(Project project) {
		JSONObject jsonProject = new JSONObject().element("id", project.getId()).element("name", project.getName());
		return jsonProject;
	}

}
