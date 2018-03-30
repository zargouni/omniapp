package com.omniacom.omniapp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.JsonResponse;

@RestController
public class ProjectController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectService projectService;

	private Project currentProject;

	@GetMapping("/project")
	public ModelAndView index(Model model, @RequestParam("id") long projectId) {
//		setCurrentProject(projectService.findOneById(projectId));
		return new ModelAndView("project");
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
	public ModelAndView dashboard() {
		return new ModelAndView("fragments/project-fragments/fragment-dashboard :: fragment-dashboard");
	}

	@GetMapping("/feed")
	public ModelAndView feed() {
		return new ModelAndView("fragments/project-fragments/fragment-feed :: fragment-feed");
	}

	@GetMapping("/operations")
	public ModelAndView operations() {
		return new ModelAndView("fragments/project-fragments/fragment-operations :: fragment-operations");
	}

	@GetMapping("/tasks")
	public ModelAndView tasks() {
		return new ModelAndView("fragments/project-fragments/fragment-tasks :: fragment-tasks");
	}

	@GetMapping("/issues")
	public ModelAndView issues() {
		return new ModelAndView("fragments/project-fragments/fragment-issues :: fragment-issues");
	}

	@GetMapping("/calendar")
	public ModelAndView calendar() {
		return new ModelAndView("fragments/project-fragments/fragment-calendar :: fragment-calendar");
	}

//	@ModelAttribute(name = "currentProject")
//	public Project getCurrentProject() {
//		return currentProject;
//	}
//
//	public void setCurrentProject(Project currentProject) {
//		this.currentProject = currentProject;
//	}

}
