package com.omniacom.omniapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.service.ClientService;
import com.omniacom.omniapp.service.ProjectService;

@ControllerAdvice
@RequestMapping("*/add-project")
public class FormControllerAdvice extends ResponseEntityExceptionHandler  {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ClientService clientService;
	
		
	@PostMapping
	public String addProject(@ModelAttribute("project") Project project, HttpServletRequest request) {
		projectService.addProject(project);
		return "index";
	}
	
	@ModelAttribute(name="project")
	public Project initProject() {
		return new Project();
	}
	
	@ModelAttribute(name="clients")
	public Iterable<Client> getClients() {
		return clientService.findAllClients();
	}
	
}
