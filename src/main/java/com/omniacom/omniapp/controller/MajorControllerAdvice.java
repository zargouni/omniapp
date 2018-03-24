package com.omniacom.omniapp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.service.ClientService;
import com.omniacom.omniapp.service.IAuthenticationFacade;
import com.omniacom.omniapp.service.ProjectService;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.JsonResponse;
import com.omniacom.omniapp.validator.ProjectValidator;
import com.omniacom.omniapp.zohoAPI.ProjectsAPI;

@ControllerAdvice
@RequestMapping("/")
@SessionAttributes("auth")
public class MajorControllerAdvice extends ResponseEntityExceptionHandler {

	@Autowired
	private ProjectValidator projectValidator;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ClientService clientService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectsAPI projectsApi;
	
	//Change other controllers to match this one me tensesh
	@Autowired
    private IAuthenticationFacade authenticationFacade;
	
	//@ModelAttribute(name="auth")
	public Authentication getAuth() {
		Authentication auth = authenticationFacade.getAuthentication();
		return auth;
	}
	
	//@ModelAttribute(name="sessionUser") 
	public User getSessionUser() {
		return userService.findByUserName(getAuth().getName());
	}
	//yeah yeah
	
	@GetMapping("/")
	public String index(Model model) {
		//model.addAttribute("addProjectFormAction", "home/add-project");
		if(getSessionUser().getZohoToken() == null)
		return "redirect:/zoho";
		return "redirect:/home";
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(projectValidator);
	}

	@PostMapping("*/add-project")
	public @ResponseBody JsonResponse addProject(@ModelAttribute("project") @Validated Project project,
			BindingResult result) throws IOException {

		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			projectService.addProject(project);
			
			response.setStatus("SUCCESS");
			projectsApi.pushProject(project,getSessionUser());
			// response.setResult(userList);
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@ModelAttribute(name = "project")
	public Project initProject() {
		return new Project();
	}

	@ModelAttribute(name = "clients")
	public Iterable<Client> getClients() {
		return clientService.findAllClients();
	}
	
	

}
