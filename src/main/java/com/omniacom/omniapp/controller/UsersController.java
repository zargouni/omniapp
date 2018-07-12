package com.omniacom.omniapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.service.UserService;

import net.sf.json.JSONArray;

@RestController
public class UsersController {

	@Autowired
	UserService userService;
	
	@GetMapping("/users")
	public @ResponseBody ModelAndView index(Model model) {
		return new ModelAndView("users");
	}
	
	@GetMapping("/get-all-app-users")
	public @ResponseBody JSONArray getAllUsers() {
		return userService.findAllUsersDetailed();
	}
}
