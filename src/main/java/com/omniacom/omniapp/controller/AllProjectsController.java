package com.omniacom.omniapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.service.IAuthenticationFacade;
import com.omniacom.omniapp.service.UserService;



@Controller
public class AllProjectsController {
	
	@Autowired
    private IAuthenticationFacade authenticationFacade;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/projects")
	public String index(Model model) {
		model.addAttribute("addProjectFormAction", "projects/add-project");
		return "projects";
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
