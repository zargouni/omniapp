package com.omniacom.omniapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Notification;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.TaskTemplate;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.service.BoqService;
import com.omniacom.omniapp.service.ClientService;
import com.omniacom.omniapp.service.NotificationService;
import com.omniacom.omniapp.service.OperationService;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.service.ServiceService;
import com.omniacom.omniapp.service.ServiceTemplateService;
import com.omniacom.omniapp.service.SiteService;
import com.omniacom.omniapp.service.TaskService;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.BoqValidator;
import com.omniacom.omniapp.validator.ClientValidator;
import com.omniacom.omniapp.validator.JsonResponse;
import com.omniacom.omniapp.validator.OperationValidator;
import com.omniacom.omniapp.validator.ProjectValidator;
import com.omniacom.omniapp.validator.ServiceValidator;
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
	private BoqValidator boqValidator;

	@Autowired
	private ServiceValidator serviceValidator;

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
	private ServiceTemplateService stService;

	@Autowired
	private BoqService boqService;

	@Autowired
	private ServiceService serviceService;

	@Autowired
	private ProjectsAPI projectsApi;
	
	@Autowired
	private NotificationService notificationService;

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
		model.addAttribute("serviceTemplateList", stService.findAllServiceTemplates());
		model.addAttribute("boq", new BillOfQuantities());
		model.addAttribute("notifications", notificationService.findByUser(userService.getSessionUser(),10));

	}

	// private List<Service> selectedProjectServices;

	@GetMapping("/get-all-users-json")
	public @ResponseBody JSONArray getAllUsers() {
		return userService.findAllUsers();
	}

	@GetMapping("/get-all-users-json-details")
	public @ResponseBody JSONArray getAllUsersDetailsJson() {
		return userService.findAllUsersDetails();
	}

	@GetMapping("/")
	public String index(Model model) {
		if (userService.getSessionUser().getZohoToken() == null)
			return "redirect:/zoho";

		return "redirect:/home";
	}

	@PostMapping("/add-boq")
	public @ResponseBody JsonResponse addBoq(@ModelAttribute("boq") @Validated BillOfQuantities boq,
			BindingResult result) throws IOException {

		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			if (!boqService.boqNameExists(boq.getName())) {
				long addedBoqId = boqService.addBoq(boq).getId();
				response.setStatus("SUCCESS");
				response.setResult(addedBoqId);
			} else {
				response.setStatus("FAIL");
				response.setResult("boq-exists");

			}

			// projectsApi.pushProject(project, getSessionUser());
			// response.setResult(userList);
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@GetMapping("/set-selected-project-client-sites-by-nature")
	public @ResponseBody JSONArray setSelectOperationSite(@RequestParam("projectId") long projectId) {
		Project project = projectService.findOneById(projectId);
		JSONArray sites = new JSONArray();
		if (project != null) {
			sites = clientService.findAllSitesByNatureJson(project.getNature(), project.getClient());
		}
		return sites;
	}

	@GetMapping("/set-select-boq")
	public @ResponseBody JSONArray setSelectBoq() {
		List<BillOfQuantities> boqs = (List<BillOfQuantities>) boqService.findAllAvailableValidBoqs();
		List<JSONObject> jsonArray = new ArrayList<JSONObject>();
		for (BillOfQuantities boq : boqs) {
			jsonArray.add(boqService.jsonBoq(boq));
		}
		JSONArray json = JSONArray.fromObject(jsonArray);
		return json;
	}

	@PostMapping("/add-service-template-to-boq")
	public @ResponseBody JsonResponse addServiceTemplateToBoq(@RequestParam("id") long templateId,
			@RequestParam("boqId") long boqId, @RequestParam("price") String price) throws IOException {
		float priceHT = Float.valueOf(price);
		JsonResponse response = new JsonResponse();

		ServiceTemplate template = stService.findOne(templateId);
		BillOfQuantities boq = boqService.findOne(boqId);
		if (template != null && boq != null) {
			if (!boqService.addOneServiceTemplate(boq, template, priceHT))
				response.setStatus("FAIL");
		}
		return response;
	}

	@PostMapping("/add-project-to-boq")
	public @ResponseBody JsonResponse addProjectToBoq(@RequestParam("projectId") long projectId,
			@RequestParam("boqId") long boqId) throws IOException {

		JsonResponse response = new JsonResponse();

		Project project = projectService.findOneById(projectId);
		BillOfQuantities boq = boqService.findOne(boqId);
		if (project != null && boq != null) {
			boq.setProject(project);
			boqService.updateBoq(boq.getId());
			project.setBoq(boq);
			projectService.save(project);
			response.setStatus("SUCCESS");
			return response;
		}
		response.setStatus("FAIL");
		return response;
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

	@GetMapping("/get-project-operations")
	public @ResponseBody JSONArray getProjectOperations(@RequestParam("projectId") long projectId) {
		return projectService.getAllOperationsJson(projectId);
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

	@GetMapping("/get-service-templates-from-project-boq")
	public @ResponseBody JSONArray getServiceTemplatesFromProjectBoq(@RequestParam("projectId") long projectId) {
		Project project = projectService.findOneById(projectId);
		List<JSONObject> jsonArray = new ArrayList<JSONObject>();

		if (project != null) {
			List<BillOfQuantities> boqs = projectService.findAllBoqs(project);
			if (!boqs.isEmpty()) {
				for (BillOfQuantities boq : boqs) {
					for (ServiceTemplate st : boqService.findAllServiceTemplates(boq)) {
						jsonArray.add(stService.jsonServiceTemplate(st));
					}
				}
			}
		}
		JSONArray json = JSONArray.fromObject(jsonArray);
		return json;
	}

	@PostMapping("/add-project")
	public @ResponseBody JsonResponse addProject(@ModelAttribute("project") @Validated Project project,
			BindingResult result) throws IOException {

		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			Project addedProject = projectService.addProject(project);

			response.setStatus("SUCCESS");
			response.setResult(addedProject.getId());
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
			response.setResult(taskService.addTask(task).getId());
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@PostMapping("/add-task-owner")
	public @ResponseBody JsonResponse addUserToTask(@RequestParam("id") long taskId,
			@RequestParam("userId") long userId) {

		JsonResponse response = new JsonResponse();

		User user = userService.findById(userId);
		Task task = taskService.findOne(taskId);
		if (task != null && user != null) {
			if (!taskService.addOneOwner(task, user))
				response.setStatus("FAIL");
		}
		return response;
	}

	@PostMapping("/add-service")
	public @ResponseBody JsonResponse addService(@Validated Service service, BindingResult result) throws IOException {

		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			serviceService.addService(service);

			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@PostMapping("/add-operation")
	public @ResponseBody JsonResponse addOperation(@Validated @ModelAttribute("operation") Operation operation,
			@RequestParam("responsible") String responsibleUserName, BindingResult result) throws IOException {

		JsonResponse response = new JsonResponse();
		List<Object> resultSuccess = new ArrayList<>();
		Operation addedOperation = null;
		User responsible = userService.findByUserName(responsibleUserName);
		if (!result.hasErrors()) {
			if (responsible != null) {
				operation.setResponsible(responsible);
				addedOperation = operationService.addOperation(operation);
				resultSuccess.add(addedOperation.getId());
				resultSuccess.add(projectService.findAllBoqs(operation.getProject()).size());

				response.setStatus("SUCCESS");
				response.setResult(resultSuccess);
			} else {
				response.setStatus("FAIL");
				response.setResult("INVALIDUSER");
			}
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());

		}

		return response;
	}

	@GetMapping("/get-service-template-tasks")
	public @ResponseBody JsonResponse getServiceTemplateTasks(@RequestParam("templateId") long id) {
		JsonResponse jsonResponse = new JsonResponse();
		JSONArray array = new JSONArray();
		ServiceTemplate service = stService.findOne(id);
		if (service != null) {
			List<TaskTemplate> taskTemplates = stService.findAllTaskTemplates(service);
			for (TaskTemplate task : taskTemplates) {
				JSONObject jsonTask = new JSONObject().element("name", task.getName()).element("id", task.getId())
						.element("estimationDays", task.getEstimationTime())
						.element("estimationHR", task.getEstimationHR());
				array.add(jsonTask);
			}
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(array);
			return jsonResponse;
		}
		jsonResponse.setStatus("FAIL");
		return jsonResponse;
	}

	@PostMapping("/generate-operation-service-from-template")
	public @ResponseBody JsonResponse generateOperationServiceFromTemplate(
			@RequestParam("operationId") long operationId, @RequestParam("templateId") long templateId) {

		JsonResponse response = new JsonResponse();
		Service addedService = operationService.generateServiceFromTemplate(operationId, templateId);
		if (addedService != null) {
			response.setStatus("SUCCESS");
			response.setResult(addedService.getId());
		} else {
			response.setStatus("FAIL");
			// response.setResult(result.getFieldErrors());
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
			} else {
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
	@PostMapping("/mark-old-notifications-as-read")
	public @ResponseBody boolean readNotifications() {
		List<Notification> notifications = notificationService.findAllByUser(userService.getSessionUser());
		for(Notification notif : notifications) {
			notif.setRead(true);
			notificationService.save(notif);
		}
		return true;
	}
	

	@PostMapping("/process-excel-sites")
	public @ResponseBody JsonResponse processExcel(@RequestParam("id") long clientId,
			@RequestParam("excelfile") MultipartFile excelfile) {
		JsonResponse response = new JsonResponse();
		try {
			List<Site> sites = new ArrayList<>();
			int i = 0;
			int addedSites = 0;
			// Creates a workbook object from the uploaded excelfile
			HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
			// Creates a worksheet object representing the first sheet
			HSSFSheet worksheet = workbook.getSheetAt(0);
			// Reads the data in excel file until last row is encountered
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object for the UserInfo Model
				Site site = new Site();
				// Creates an object representing a single row in excel
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				site.setName(row.getCell(0).getStringCellValue());
				site.setLatitude(row.getCell(1).getNumericCellValue());
				site.setLongitude(row.getCell(2).getNumericCellValue());
				site.setClient(clientService.findById(clientId));
				// persist data into database in here
				if (!clientService.siteExists(clientService.findById(clientId), site)) {
					sites.add(site);
					addedSites++;
				}
			}

			siteService.addSites(sites);
			workbook.close();
			if (addedSites > 0) {
				response.setStatus("SUCCESS");
				response.setResult(addedSites);
			} else
				response.setStatus("FAIL");

			// model.addAttribute("lstUser", lstUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	// Convert objects to json format
	public JSONObject jsonService(Service service) {
		JSONObject jsonService = new JSONObject().element("id", service.getId()).element("name", service.getName());
		return jsonService;
	}

	public JSONObject jsonClient(Client client) {
		JSONObject jsonClient = new JSONObject().element("name", client.getName()).element("email", client.getEmail())
				.element("phone", client.getPhone()).element("country", client.getCountry())
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

	@InitBinder("boq")
	protected void setBoqValidator(WebDataBinder binder) {
		binder.addValidators(boqValidator);
	}

	@InitBinder("service")
	protected void setServiceValidator(WebDataBinder binder) {
		binder.addValidators(serviceValidator);
	}

}
