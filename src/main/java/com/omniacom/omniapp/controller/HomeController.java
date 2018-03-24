package com.omniacom.omniapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.service.IAuthenticationFacade;
import com.omniacom.omniapp.service.UserService;


@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private IAuthenticationFacade authenticationFacade;
	
	@GetMapping("/home")
	public String index(Model model) {
		model.addAttribute("addProjectFormAction", "home/add-project");
		return "index";
	}

	@ModelAttribute(name="tasks")
	public List<Task> getAllTasks() {
		return userService.findAllTasks(userService.findByUserName(getAuth().getName()));
	}
	
	@ModelAttribute(name="auth")
	public Authentication getAuth() {
		Authentication auth = authenticationFacade.getAuthentication();
		return auth;
	}
	
	@ModelAttribute(name="sessionUser") 
	public User getSessionUser() {
		return userService.findByUserName(getAuth().getName());
	}
	

	
	
	

	
	
	





	
	



}
