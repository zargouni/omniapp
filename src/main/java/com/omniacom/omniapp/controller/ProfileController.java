package com.omniacom.omniapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.service.UserService;

import net.sf.json.JSONArray;

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
		model.addAttribute("profileUser", userService.findById(userId));


	}
	
	@GetMapping("/json-user-tasks")
	public @ResponseBody JSONArray getAllUserTasksJson(@RequestParam("id") long userId) {
		return userService.getAllUserTasksJson(userId);
	}
	
	@GetMapping("/json-user-issues")
	public @ResponseBody JSONArray getAllUserIssuesJson(@RequestParam("id") long userId) {
		return userService.getAllUserIssuesJson(userId);
	}
	
	

}
