package com.omniacom.omniapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.service.UserService;


@RestController
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	
	
	@GetMapping("/home")
	public @ResponseBody ModelAndView index(Model model) {
		model.addAttribute("addProjectFormAction", "home/add-project");
		return new ModelAndView("index");
	}

	@ModelAttribute(name="tasks")
	public List<Task> getAllTasks() {
		return userService.findAllTasks(userService.getSessionUser());
	}
	

}
