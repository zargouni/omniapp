package com.omniacom.omniapp.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Comment;
import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Notification;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Service;
//import com.omniacom.omniapp.entity.Snag;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.UploadFileResponse;
import com.omniacom.omniapp.entity.UploadedFile;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.CommentRepository;
import com.omniacom.omniapp.repository.IssueRepository;
//import com.omniacom.omniapp.repository.SnagRepository;
import com.omniacom.omniapp.repository.UploadedFileRepository;
import com.omniacom.omniapp.service.FileStorageService;
import com.omniacom.omniapp.service.IssueService;
import com.omniacom.omniapp.service.NotificationService;
import com.omniacom.omniapp.service.OperationService;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.service.ServiceService;
import com.omniacom.omniapp.service.TaskService;
import com.omniacom.omniapp.service.UploadedFileService;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.IssueValidator;
import com.omniacom.omniapp.validator.JsonResponse;
//import com.omniacom.omniapp.validator.SnagValidator;

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

	@Autowired
	UploadedFileService fileService;

//	@Autowired
//	SnagValidator snagValidator;

	@Autowired
	IssueValidator issueValidator;

	private Date lastRefreshDateTime;

	@GetMapping("/project")
	public ModelAndView index(Model model) {

		if (projectService.getCurrentProject() == null)
			return new ModelAndView("404");
		else if (!userService.getSessionUser().getContributedProjectList().contains(projectService.getCurrentProject())
				&& projectService.getCurrentProject().getOwner() != userService.getSessionUser()
				&& !userService.getSessionUser().getRole().getName().equals("ADMIN"))
			return new ModelAndView("403");
		return new ModelAndView("project");
	}

	@ModelAttribute
	public void addAttributes(Model model, @RequestParam("id") long projectId) {
		// set current project
		projectService.setCurrentProject(projectService.findOneById(projectId));
		model.addAttribute("selectedProject", projectService.findOneById(projectId));
		model.addAttribute("taskCount", projectService.findTaskCount(projectService.getCurrentProject()));
		model.addAttribute("completedTasksCount",
				projectService.findCompletedTasksCount(projectService.getCurrentProject()));
		model.addAttribute("onGoingTasksCount",
				projectService.findOnGoingTasksCount(projectService.getCurrentProject()));
		model.addAttribute("allServices", projectService.findAllServices(projectService.getCurrentProject()));
		// System.out.println("services count:
		// "+projectService.getMapServiceTasks(projectService.getCurrentProject()).keySet().size());
		model.addAttribute("ServiceTasksMap", projectService.getMapServiceTasks(projectService.getCurrentProject()));

	}

	@GetMapping("/get-project-gantt")
	public @ResponseBody JSONArray getProjectGantt(@RequestParam("id") Project project) {
		return projectService.getGanttContent(project);
	}

	@GetMapping("/get-project-feed")
	public @ResponseBody Map<LocalDate, JSONArray> getProjectFeed(@RequestParam("id") Project project) {
		lastRefreshDateTime = new Date();
		return projectService.getProjectFeed(project);
	}

	@GetMapping("/refresh-project-feed")
	public @ResponseBody JsonResponse refreshProjectFeed(@RequestParam("id") Project project) {
		JsonResponse response = new JsonResponse();
		LocalDate latestDateInFeed = (LocalDate) projectService.getProjectFeed(project).keySet().toArray()[0];
		Set<Date> latestActivities = new TreeSet<>(projectService.getRawProjectFeed(project).get(latestDateInFeed));

		Date latestActivity = (Date) latestActivities.toArray()[latestActivities.size() - 1];

		if (lastRefreshDateTime != null) {
			if (lastRefreshDateTime.before(latestActivity)) {
				response.setStatus("REFRESH");
			} else {
				response.setStatus("NOREFRESH");
			}
		}

		return response;
	}

	@GetMapping("/get-project-operations-status")
	public @ResponseBody JSONArray getProjectOperationsStatus(@RequestParam("id") Project project) {
		return projectService.getProjectOperationsStatus(project);
	}

	@GetMapping("/get-project-calendar-events")
	public @ResponseBody JSONArray getProjectCalendarEvents(@RequestParam("id") Project project) {
		return projectService.getProjectEvents(project);
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

	@GetMapping("/json-issues")
	public @ResponseBody JSONArray getAllIssuesJson(@RequestParam("id") long projectId) {
		return issueService.getAllIssuesJson(projectId);
	}

	@GetMapping("/json-pos")
	public @ResponseBody JSONArray getAllPosJson(@RequestParam("id") long projectId) {
		return projectService.getAllPosJson(projectId);
	}

	@PostMapping("/remove-service-from-po")
	public @ResponseBody JsonResponse removeServiceFromPo(@RequestParam("id") long serviceId) {
		Service service = serviceService.findById(serviceId);
		JsonResponse response = new JsonResponse();
		service.setPoNumber("NOPO");
		if (serviceService.save(service) != null) {
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
		}
		return response;
	}

	@PostMapping("/delete-po")
	public @ResponseBody JsonResponse deletePo(@RequestParam("id") long projectId,
			@RequestParam("number") String poNumber) {
		JsonResponse response = new JsonResponse();
		boolean success = true;
		List<Service> services = serviceService.findAllByPoNumber(projectId, poNumber);
		for (Service service : services) {
			service.setPoNumber("NOPO");
			if (serviceService.save(service) == null) {
				success = false;
			}
		}
		if (success) {
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
		}
		return response;
	}

	@PostMapping("/update-service-po")
	public @ResponseBody JsonResponse updateServicePo(@RequestParam("id") long serviceId,
			@RequestParam("number") String poNumber) {
		Service service = serviceService.findById(serviceId);
		JsonResponse response = new JsonResponse();
		service.setPoNumber(poNumber);
		if (serviceService.save(service) != null) {
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
		}
		return response;
	}

	@PostMapping("/check-po-existance")
	public @ResponseBody JsonResponse checkPoExistance(@RequestParam("id") long projectId,
			@RequestParam("number") String poNumber) {
		JsonResponse response = new JsonResponse();

		if (!serviceService.findAllByPoNumber(projectId, poNumber).isEmpty()) {
			response.setStatus("EXISTS");

		} else {
			response.setStatus("SUCCESS");
		}
		return response;
	}

	@GetMapping("/json-po-services")
	public @ResponseBody JSONArray getAllPoServices(@RequestParam("id") long projectId,
			@RequestParam("number") String poNumber) {
		return projectService.getAllPoServicesJson(projectId, poNumber);
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

	@GetMapping("/get-operation-comments")
	public @ResponseBody JSONArray getOperationComments(@RequestParam("id") long operationId) {
		return operationService.getOperationComments(operationId);
	}

	@GetMapping("/get-operation-snags")
	public @ResponseBody JSONArray getOperationSnags(@RequestParam("id") long operationId) {
		return operationService.getOperationSnags(operationId);
	}

	@GetMapping("/get-task-comments")
	public @ResponseBody JSONArray getTaskComments(@RequestParam("id") long taskId) {
		return taskService.getTaskComments(taskId);
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

	private List<User> taskUsers = null;

	private List<User> issueUsers = null;

	@PostMapping("/update-task")
	public @ResponseBody JsonResponse doUpdateTask(@RequestParam("id") long taskId, @Validated Task updatedTask,
			BindingResult result) {
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			// if (!taskService.boqNameExists(boq.getName())) {
			taskUsers = taskService.findOne(taskId).getUsers();
			if (taskService.updateTask(taskId, updatedTask)) {

				response.setStatus("SUCCESS");
			} else {
				response.setStatus("FAIL");
			}
			// } else if (boq.getName().equals(boqService.findOne(boqId).getName())) {
			// if (boqService.updateBoq(boqId, boq)) {

			// response.setStatus("SUCCESS");
			// } else {
			// response.setStatus("FAIL");
			// }
			// } else {

			// response.setStatus("FAIL");
			// response.setResult("boq-exists");

			// }

		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@PostMapping("/delete-task")
	public @ResponseBody JsonResponse doDeleteTask(@RequestParam("id") long taskId) {
		JsonResponse response = new JsonResponse();
		Task task = taskService.findOne(taskId);
		if (task != null) {
			taskService.deleteTask(task);
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
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

	@GetMapping("/get-all-users-in-task-details-json")
	public @ResponseBody JSONArray getAllUsersForTask(@RequestParam("id") long taskId) {
		return taskService.findAllUsersForTask(taskId);
	}

	@Autowired
	NotificationService notificationService;

	@PostMapping("/update-task-owners")
	public @ResponseBody JsonResponse addUserToTask(@RequestParam("id") long taskId,
			@RequestParam("userId") long userId) {

		JsonResponse response = new JsonResponse();

		// ServiceTemplate template = stService.findOne(templateId);
		// Site site = siteService.findSite(siteId);
		User user = userService.findById(userId);
		Task task = taskService.findOne(taskId);
		if (task != null && user != null) {
			if (!taskService.addOneOwner(task, user))
				response.setStatus("FAIL");
			else {
				// Send notification to added users
				if (!taskUsers.contains(user)) {
					notificationService.sendNotification(user, task);
				}

				// Add contributing user to task's project
				if (task.getService().getOperation() != null)
					userService.addContributingUserToProject(user, task.getService().getOperation().getProject());
				else
					userService.addContributingUserToProject(user, task.getService().getProject());

			}
		}
		return response;
	}

	@Autowired
	CommentRepository commentRepo;

	@PostMapping("/do-post-comment")
	public JsonResponse doPostComment(@RequestParam("id") long operationId, @RequestParam("content") String content) {
		JsonResponse response = new JsonResponse();
		if (content.length() != 0 && operationService.findOne(operationId) != null) {
			Comment comment = new Comment();
			comment.setContent(content);
			comment.setOperation(operationService.findOne(operationId));
			comment.setDate(new Date());
			comment.setUser(userService.getSessionUser());
			if (commentRepo.save(comment) != null) {
				response.setStatus("SUCCESS");
			} else {
				response.setStatus("FAIL");
			}
		} else {
			response.setStatus("NOCONTENT");
		}
		return response;
	}

	@PostMapping("/do-post-task-comment")
	public JsonResponse doPostTaskComment(@RequestParam("id") long taskId, @RequestParam("content") String content) {
		JsonResponse response = new JsonResponse();
		if (content.length() != 0 && taskService.findOne(taskId) != null) {
			Comment comment = new Comment();
			comment.setContent(content);
			comment.setTask(taskService.findOne(taskId));
			comment.setDate(new Date());
			comment.setUser(userService.getSessionUser());
			if (commentRepo.save(comment) != null) {
				response.setStatus("SUCCESS");
			} else {
				response.setStatus("FAIL");
			}
		} else {
			response.setStatus("NOCONTENT");
		}
		return response;
	}

//	@Autowired
//	private SnagRepository snagRepo;

//	@PostMapping("/do-post-snag")
//	public JsonResponse doPostSnag(@RequestParam("id") long operationId, @Validated Snag snag, BindingResult result) {
//		JsonResponse response = new JsonResponse();
//		Operation operation = operationService.findOne(operationId);
//		if (!result.hasErrors() && operation != null) {
//			snag.setOperation(operation);
//			snag.setUser(userService.getSessionUser());
//			snag.setDate(new Date());
//			if (snagRepo.save(snag) != null) {
//				response.setStatus("SUCCESS");
//			} else {
//				response.setStatus("FAIL");
//			}
//
//		} else if (result.hasErrors()) {
//			response.setStatus("FAIL");
//			response.setResult(result.getFieldErrors());
//		}
//
//		return response;
//	}

	@Autowired
	IssueRepository issueRepo;

	@PostMapping("/add-issue")
	public JsonResponse doAddIssue(@RequestParam("id") long projectId, @Validated Issue issue, BindingResult result) {
		JsonResponse response = new JsonResponse();
		// Operation operation = operationService.findOne(operationId);
		Project project = projectService.findOneById(projectId);
		if (!result.hasErrors() && project != null) {
			issue.setProject(project);
			// issue.setOperation(operation);
			issue.setId(0);
			issue.setCreator(userService.getSessionUser());
			issue.setCreationDate(new Date());
			issue.setStatus(StaticString.ISSUE_STATUS_OPEN);
			issue.setAttachments(new ArrayList<UploadedFile>());
			issue.setComments(new ArrayList<Comment>());
			issue.setNotifications(new ArrayList<Notification>());
			Issue savedIssue = issueRepo.save(issue);
			if (savedIssue != null) {
				response.setStatus("SUCCESS");
				response.setResult(savedIssue.getId());
			} else {
				response.setStatus("FAIL");
			}

		} else if (result.hasErrors()) {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@Autowired
	IssueService issueService;

	@PostMapping("/add-issue-owner")
	public @ResponseBody JsonResponse addUserToIssue(@RequestParam("id") long issueId,
			@RequestParam("userId") long userId) {

		JsonResponse response = new JsonResponse();

		User user = userService.findById(userId);
		Issue issue = issueRepo.findOne(issueId);
		if (issue != null && user != null) {
			if (!issueService.addOneOwner(issue, user))
				response.setStatus("FAIL");
			else {

			}
		}
		return response;
	}

	@PostMapping("/delete-issue")
	public @ResponseBody JsonResponse doDeleteIssue(@RequestParam("id") long issueId) {
		JsonResponse response = new JsonResponse();
		Issue issue = issueService.findOne(issueId);
		if (issue != null) {
			issueService.deleteIssue(issue);
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
		}
		return response;
	}
	
//	@PostMapping(value = "/upload")
//	public ResponseEntity handleFileUpload(@RequestParam("id") long id, @RequestParam("file") MultipartFile[] files) {
//		boolean success = true;
//		UploadedFile dbFile = new UploadedFile();
//		for (int i = 0; i < files.length; i++) {
//			try {
//				fileService.saveFileToLocalDisk(files[i]);
//				dbFile.setName(files[i].getOriginalFilename());
//				dbFile.setSize(files[i].getSize());
//				dbFile.setType(files[i].getContentType());
//				dbFile.setCreationDate(new Date());
//				dbFile.setLocation(fileService.getDestinationLocation());
//				dbFile.setTask(taskService.findOne(id));
//
//				fileService.saveFileToDatabase(dbFile);
//
//			} catch (IOException e) {
//				success = false;
//			}
//		}
//		if (success)
//			return ResponseEntity.status(HttpStatus.ACCEPTED).body("All Files uploaded");
//
//		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Some or all files were not uploaded");
//
//	}

//	@Autowired
//	UploadedFileRepository fileRepo;

//	@GetMapping("/attachment")
//	public void handleFileDownload(HttpServletResponse response, @RequestParam("id") long id) throws IOException {
//		File file = null;
//		UploadedFile dbFile = fileRepo.findOne(id);
//		if (dbFile != null) {
//			file = new File(dbFile.getLocation() + "" + dbFile.getName());
//		} else {
//			String errorMessage = "Sorry. The file you are looking for does not exist";
//			System.out.println(errorMessage);
//			OutputStream outputStream = response.getOutputStream();
//			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
//			outputStream.close();
//			return;
//		}
//
//		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
//		if (mimeType == null) {
//			System.out.println("mimetype is not detectable, will take default");
//			mimeType = "application/octet-stream";
//		}
//
//		System.out.println("mimetype : " + mimeType);
//
//		response.setContentType(mimeType);
//
//		/*
//		 * "Content-Disposition : inline" will show viewable types [like
//		 * images/text/pdf/anything viewable by browser] right on browser while
//		 * others(zip e.g) will be directly downloaded [may provide save as popup, based
//		 * on your browser setting.]
//		 */
//		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
//
//		/*
//		 * "Content-Disposition : attachment" will be directly download, may provide
//		 * save as popup, based on your browser setting
//		 */
//		// response.setHeader("Content-Disposition", String.format("attachment;
//		// filename=\"%s\"", file.getName()));
//
//		response.setContentLength((int) file.length());
//
//		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
//
//		// Copy bytes from source to destination(outputstream in this example), closes
//		// both streams.
//		FileCopyUtils.copy(inputStream, response.getOutputStream());
//	}

	

	@GetMapping("/get-issue-details")
	public @ResponseBody JSONObject getIssueDetails(@RequestParam("id") long issueId) {
		Issue issue = issueService.findOne(issueId);
		if (issue != null)
			return issueService.jsonIssue(issue);
		return new JSONObject();
	}

	@GetMapping("/get-issue-parents")
	public @ResponseBody JSONObject getIssueParents(@RequestParam("id") long issueId) {
		return issueService.getIssueParents(issueId);
	}

	@GetMapping("/get-all-users-in-issue-details-json")
	public @ResponseBody JSONArray getAllUsersForIssue(@RequestParam("id") long issueId) {
		return issueService.findAllUsersForIssue(issueId);
	}

	

	@PostMapping("/update-issue")
	public @ResponseBody JsonResponse doUpdateIssue(@RequestParam("id") long issueId, @Validated Issue updatedIssue,
			BindingResult result) {
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			issueUsers = issueService.findOne(issueId).getAssignedUsers();
			if (issueService.updateIssue(issueId, updatedIssue)) {

				response.setStatus("SUCCESS");
			} else {
				response.setStatus("FAIL");
			}

		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@PostMapping("/update-issue-owners")
	public @ResponseBody JsonResponse addUserToIssueUpdate(@RequestParam("id") long issueId,
			@RequestParam("userId") long userId) {

		JsonResponse response = new JsonResponse();

		// ServiceTemplate template = stService.findOne(templateId);
		// Site site = siteService.findSite(siteId);
		User user = userService.findById(userId);
		Issue issue = issueService.findOne(issueId);
		if (issue != null && user != null) {
			if (!issueService.addOneOwner(issue, user))
				response.setStatus("FAIL");
			else {
				// Send notification to added users
				if (!issueUsers.contains(user)) {
					notificationService.sendIssueNotification(user, issue);
				}

				// Add contributing user to task's project
				// if (task.getService().getOperation() != null)
				userService.addContributingUserToProject(user, issue.getProject());
				// else
				// userService.addContributingUserToProject(user,
				// task.getService().getProject());

			}
		}
		return response;
	}

	@GetMapping("/get-issue-comments")
	public @ResponseBody JSONArray getIssueComments(@RequestParam("id") long issueId) {
		return issueService.getIssueComments(issueId);
	}

	@PostMapping("/do-post-issue-comment")
	public JsonResponse doPostIssueComment(@RequestParam("id") long issueId, @RequestParam("content") String content) {
		JsonResponse response = new JsonResponse();
		if (content.length() != 0 && issueService.findOne(issueId) != null) {
			Comment comment = new Comment();
			comment.setContent(content);
			comment.setIssue(issueService.findOne(issueId));
			comment.setDate(new Date());
			comment.setUser(userService.getSessionUser());
			if (commentRepo.save(comment) != null) {
				response.setStatus("SUCCESS");
			} else {
				response.setStatus("FAIL");
			}
		} else {
			response.setStatus("NOCONTENT");
		}
		return response;
	}

	@GetMapping("/get-project-issues-stats")
	public @ResponseBody JSONObject getProjectIssuesStats(@RequestParam("id") long projectId) {
		return projectService.getProjectIssuesStats(projectId);
	}

	@PostMapping("/delete-comment")
	public @ResponseBody JsonResponse deleteComment(@RequestParam("id") long commentId) {
		JsonResponse response = new JsonResponse();
		Comment comment = commentRepo.findOne(commentId);
		if (comment != null) {
			commentRepo.delete(comment);
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
		}
		return response;
	}
	
	@PostMapping("/delete-operation")
	public @ResponseBody JsonResponse deleteOperation(@RequestParam("id") long operationId) {
		JsonResponse response = new JsonResponse();

		if (operationService.deleteOperation(operationId)) {

			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			// response.setResult(result.getFieldErrors());
		}
		return response;
	}
	
	@PostMapping("/update-operation")
	public @ResponseBody JsonResponse doUpdateOperation(@RequestParam("id") long operationId, @Validated Operation updatedOperation,
			@RequestParam("responsible") String responsibleUserName,
			BindingResult result) {
		JsonResponse response = new JsonResponse();

		Operation oldVersionOperation = operationService.findOne(operationId);
		User responsible = userService.findByUserName(responsibleUserName);
		
		if (!result.hasErrors()) {
			if (responsible != null) {
				if(!updatedOperation.equals(oldVersionOperation)) {
					operationService.updateOperation(oldVersionOperation, updatedOperation, responsible);
					response.setStatus("SUCCESS");
				}
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
	
	@PostMapping("/update-service")
	public @ResponseBody JsonResponse doUpdateService(@RequestParam("id") long serviceId, @Validated Service updatedService,
			BindingResult result) {
		JsonResponse response = new JsonResponse();

		Service oldVersionService = serviceService.findById(serviceId);
		//User responsible = userService.findByUserName(responsibleUserName);
		
		if (!result.hasErrors()) {
			//if (responsible != null) {
				if(!updatedService.equals(oldVersionService)) {
					serviceService.updateService(oldVersionService, updatedService);
					response.setStatus("SUCCESS");
				}
//			} else {
//				response.setStatus("FAIL");
//				response.setResult("INVALIDUSER");
//			}
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}
	
	@PostMapping("/delete-service")
	public @ResponseBody JsonResponse deleteService(@RequestParam("id") long serviceId) {
		JsonResponse response = new JsonResponse();

		if (serviceService.deleteService(serviceId)) {

			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			// response.setResult(result.getFieldErrors());
		}
		return response;
	}
	
	@PostMapping("/delete-project")
	public @ResponseBody JsonResponse deleteProject(@RequestParam("id") long projectId) {
		JsonResponse response = new JsonResponse();

		if (projectService.deleteProject(projectId)) {

			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			// response.setResult(result.getFieldErrors());
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

//	@InitBinder("snag")
//	protected void setSnagValidator(WebDataBinder binder) {
//		binder.addValidators(snagValidator);
//	}

	@InitBinder("issue")
	protected void setIssueValidator(WebDataBinder binder) {
		binder.addValidators(issueValidator);
	}
}
