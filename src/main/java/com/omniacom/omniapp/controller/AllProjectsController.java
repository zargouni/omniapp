package com.omniacom.omniapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.service.UserService;

import net.sf.json.JSONArray;

@RestController
public class AllProjectsController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectService projectService;

	@GetMapping("/projects")
	public ModelAndView index(Model model) {
		return new ModelAndView("projects");
	}

	@GetMapping("/get-all-projects")
	public @ResponseBody JSONArray getAllProjects() {
		JSONArray jsonArray = new JSONArray();
		List<Project> projects = userService.findAllContributedProjects(userService.getSessionUser());
		for (Project p : projects) {
			jsonArray.add(projectService.jsonProject(p));
		}
		return jsonArray;
	}

}
