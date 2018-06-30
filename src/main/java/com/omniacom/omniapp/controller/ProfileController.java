package com.omniacom.omniapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.service.UserService;

@RestController
public class ProfileController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/profile")
	public ModelAndView index(@RequestParam("id") long userId,Model model) {

		if (userService.findById(userId) == null)
			return new ModelAndView("404");
		return new ModelAndView("profile");
	}
	
	@ModelAttribute
	public void addAttributes(Model model, @RequestParam("id") long userId) {
		// set current project
		//projectService.setCurrentProject(projectService.findOneById(projectId));
		model.addAttribute("profileUser", userService.findById(userId));
//		model.addAttribute("taskCount", projectService.findTaskCount(projectService.getCurrentProject()));
//		model.addAttribute("completedTasksCount",
//				projectService.findCompletedTasksCount(projectService.getCurrentProject()));
//		model.addAttribute("onGoingTasksCount",
//				projectService.findOnGoingTasksCount(projectService.getCurrentProject()));
//		model.addAttribute("allServices", projectService.findAllServices(projectService.getCurrentProject()));
//		model.addAttribute("ServiceTasksMap", projectService.getMapServiceTasks(projectService.getCurrentProject()));

	}

}
