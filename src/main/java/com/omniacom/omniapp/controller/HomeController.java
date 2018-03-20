package com.omniacom.omniapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.thymeleaf.context.Context;

import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.service.ClientService;
import com.omniacom.omniapp.service.IAuthenticationFacade;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.service.UserService;

@Controller
@SessionAttributes("project")
public class HomeController {
	
	@Autowired
    private IAuthenticationFacade authenticationFacade;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ClientService clientService;
	
	@GetMapping("/home")
	public String index(Model model) {
		model.addAttribute("project", new Project());
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
	

	@ModelAttribute(name="clients")
	public Iterable<Client> getClients() {
		return clientService.findAllClients();
	}
	
	@PostMapping("/home")
	public String addProject(@ModelAttribute("project") Project project) {
		projectService.addProject(project);
		return "index";
	}
	





	
	



}
