package com.omniacom.omniapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.service.UserService;

@RestController
public class AllProjectsController {

	@Autowired
	private UserService userService;

	@GetMapping("/projects")
	public ModelAndView index(Model model) {
		return new ModelAndView("projects");
	}

	@ModelAttribute(name = "allProjects")
	public List<Project> getAllProjects() {
		return userService.findAllContributedProjects(userService.getSessionUser());
	}
	
}
