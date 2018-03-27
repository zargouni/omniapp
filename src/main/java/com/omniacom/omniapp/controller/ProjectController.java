package com.omniacom.omniapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.service.IAuthenticationFacade;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.JsonResponse;

@Controller
public class ProjectController {

	@Autowired
	private IAuthenticationFacade authenticationFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectService projectService;

	private Project currentProject;

	@ModelAttribute(name = "auth")
	public Authentication getAuth() {
		Authentication auth = authenticationFacade.getAuthentication();
		return auth;
	}

	@ModelAttribute(name = "sessionUser")
	public User getSessionUser() {
		return userService.findByUserName(getAuth().getName());
	}

	@GetMapping("/project")
	public String project(Model model, @RequestParam("id") long projectId) {
		setCurrentProject(projectService.findOneById(projectId));
		return "project";
	}

	@GetMapping("/get-global-task-status")
	public @ResponseBody JsonResponse getGlobalTaskStatus() throws IOException {

		JsonResponse response = new JsonResponse();
		List<Integer> statusCount = projectService.findTaskStatusCount(currentProject);
		if (statusCount.get(0) == 0 && statusCount.get(1) == 0) {
			response.setStatus("FAIL");
			return response;
		}
		response.setStatus("SUCCESS");
		response.setResult(statusCount);
		return response;
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		return "fragments/project-fragments/fragment-dashboard :: fragment-dashboard";
	}

	@GetMapping("/feed")
	public String feed() {
		return "fragments/project-fragments/fragment-feed :: fragment-feed";
	}

	@GetMapping("/operations")
	public String operations() {
		return "fragments/project-fragments/fragment-operations :: fragment-operations";
	}

	@GetMapping("/tasks")
	public String tasks() {
		return "fragments/project-fragments/fragment-tasks :: fragment-tasks";
	}

	@GetMapping("/issues")
	public String issues() {
		return "fragments/project-fragments/fragment-issues :: fragment-issues";
	}

	@GetMapping("/calendar")
	public String calendar() {
		return "fragments/project-fragments/fragment-calendar :: fragment-calendar";
	}

	@ModelAttribute(name = "currentProject")
	public Project getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}

}
