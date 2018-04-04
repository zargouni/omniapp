package com.omniacom.omniapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.service.ClientService;
import com.omniacom.omniapp.service.OperationService;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.service.SiteService;
import com.omniacom.omniapp.service.TaskService;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.ClientValidator;
import com.omniacom.omniapp.validator.JsonResponse;
import com.omniacom.omniapp.validator.OperationValidator;
import com.omniacom.omniapp.validator.ProjectValidator;
import com.omniacom.omniapp.validator.SiteValidator;
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
	private OperationValidator operationValidator;

	@Autowired
	private TaskValidator taskValidator;

	@Autowired
	private ClientValidator clientValidator;

	@Autowired
	private SiteValidator siteValidator;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private OperationService operationService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private UserService userService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ProjectsAPI projectsApi;

	@ModelAttribute
	public void addAtributes(Model model) {
		model.addAttribute("TASK_STATUS_ONGOING", this.TASK_STATUS_ONGOING);
		model.addAttribute("TASK_STATUS_COMPLETED", this.TASK_STATUS_COMPLETED);
		model.addAttribute("project", new Project());
		model.addAttribute("clients", clientService.findAllClients());
		model.addAttribute("task", new Task());
		model.addAttribute("selectedProject", new Project());
		model.addAttribute("sessionUser", userService.getSessionUser());
		model.addAttribute("newOperation", new Operation());
		model.addAttribute("client", new Client());
		model.addAttribute("site", new Site());
		model.addAttribute("addedClient", this.newClient);

	}

	// private List<Service> selectedProjectServices;

	@GetMapping("/")
	public String index(Model model) {
		if (userService.getSessionUser().getZohoToken() == null)
			return "redirect:/zoho";

		return "redirect:/home";
	}

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
			task.setStatus(TASK_STATUS_ONGOING);
			taskService.addTask(task);
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@PostMapping("/add-operation")
	public @ResponseBody JsonResponse addOperation(@Validated @ModelAttribute("operation") Operation operation,
			BindingResult result) throws IOException {

		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			operationService.addOperation(operation);
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	List<Site> addedSiteList;
	private Client newClient;

	@PostMapping("/wizard-save-client")
	public @ResponseBody JsonResponse saveClient(@Validated @ModelAttribute("client") Client client,
			BindingResult result) throws IOException {
		addedSiteList = new ArrayList<Site>();
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			if (!clientService.clientExists(client)) {
				this.newClient = client;
				response.setStatus("SUCCESS");
			}else {
				response.setStatus("FAIL");
				response.setResult("existing.client");
			}
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@GetMapping("/get-added-sites-wizard")
	public @ResponseBody JSONArray getAddedSites() {
		List<JSONObject> jsonArray = new ArrayList<JSONObject>();
		for (Site s : addedSiteList) {
			jsonArray.add(jsonSite(s));
		}
		JSONArray json = JSONArray.fromObject(jsonArray);
		return json;
	}
	
	@GetMapping("/get-added-client-wizard")
	public @ResponseBody JSONObject getAddedClient() {
		
		JSONObject json = JSONObject.fromObject(jsonClient(newClient));
		return json;
	}

	@Autowired
	private SiteService siteService;

	@PostMapping("/wizard-save-site")
	public @ResponseBody JsonResponse saveSite(@Validated @ModelAttribute("site") Site site, BindingResult result)
			throws IOException {

		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			site.setClient(newClient);
			if (!addedSiteList.contains(site))
				addedSiteList.add(site);

			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@PostMapping("/client-wizard-publish")
	public @ResponseBody boolean saveNewClientWizard() {
		clientService.addClient(newClient);
		siteService.addSites(addedSiteList);

		addedSiteList.clear();
		return true;
	}

	// Convert objects to json format
	public JSONObject jsonService(Service service) {
		JSONObject jsonService = new JSONObject().element("id", service.getId()).element("name", service.getName());
		return jsonService;
	}
	
	public JSONObject jsonClient(Client client) {
		JSONObject jsonClient = new JSONObject()
						.element("name", client.getName())
						.element("email", client.getEmail())
						.element("phone", client.getPhone())
						.element("country", client.getCountry())
						.element("address", client.getAddress());
		return jsonClient;
	}

	public JSONObject jsonProject(Project project) {
		JSONObject jsonProject = new JSONObject().element("id", project.getId()).element("name", project.getName());
		return jsonProject;
	}

	public JSONObject jsonSite(Site site) {
		JSONObject jsonSite = new JSONObject().element("name", site.getName()).element("longitude", site.getLongitude())
				.element("latitude", site.getLatitude());
		return jsonSite;
	}

	// Set validators
	@InitBinder("project")
	public void setProjectValidator(WebDataBinder binder) {
		binder.addValidators(projectValidator);
	}

	@InitBinder("task")
	protected void setTaskValidator(WebDataBinder binder) {
		binder.addValidators(taskValidator);
	}

	@InitBinder("operation")
	protected void setOperationValidator(WebDataBinder binder) {
		binder.addValidators(operationValidator);
	}

	@InitBinder("client")
	protected void setClientValidator(WebDataBinder binder) {
		binder.addValidators(clientValidator);
	}

	@InitBinder("site")
	protected void setSiteValidator(WebDataBinder binder) {
		binder.addValidators(siteValidator);
	}

}