package com.omniacom.omniapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.service.IAuthenticationFacade;
import com.omniacom.omniapp.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
    private IAuthenticationFacade authenticationFacade;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/home")
	public String index() {
		return "index";
	}

	@ModelAttribute(name="tasks")
	public List<Task> getAllTasks() {
		return userService.findAllTasks(userService.findByUserName(getAuth().getName()));
	}

	
	@ModelAttribute(name="auth")
	public Authentication getAuth() {
		return authenticationFacade.getAuthentication();
	}

	





	
	



}
