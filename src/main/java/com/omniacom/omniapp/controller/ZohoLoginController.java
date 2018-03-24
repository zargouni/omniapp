package com.omniacom.omniapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.omniacom.omniapp.controller.form.ZohoLoginForm;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.service.IAuthenticationFacade;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.zohoAPI.UsersAPI;

@Controller
public class ZohoLoginController {

	@Autowired
	private UsersAPI usersApi;

	@Autowired
	private UserService userService;

	@Autowired
	private IAuthenticationFacade authenticationFacade;
	
	
	public Authentication getAuth() {
		Authentication auth = authenticationFacade.getAuthentication();
		return auth;
	}

	public User getSessionUser() {
		return userService.findByUserName(getAuth().getName());
	}

	@PostMapping("/zoho")
	public String doGetAuthToken(@ModelAttribute("zohoLoginForm") ZohoLoginForm zohoForm,Model model) {
		boolean zohoLoginResult = usersApi.updateLocalUserFromZoho(getSessionUser(), zohoForm.getEmail(), zohoForm.getPassword());
		if(zohoLoginResult)
			return "redirect:/home";
		else
			model.addAttribute("result", false);
		return "login-zoho";
	}

	@GetMapping("/zoho")
	public String index(Model model) {
		model.addAttribute("zohoLoginForm", new ZohoLoginForm());
		return "login-zoho";
	}
	
	
}
