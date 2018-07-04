package com.omniacom.omniapp.controller;

import java.time.LocalDate;
import java.util.Map;

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
import net.sf.json.JSONObject;

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
	
	@GetMapping("/json-user-stats")
	public @ResponseBody JSONObject getUserStats(@RequestParam("id") long userId) {
		return userService.getUserStatsJson(userId);
	}
	
	@GetMapping("/json-user-tasks")
	public @ResponseBody JSONArray getAllUserTasksJson(@RequestParam("id") long userId) {
		return userService.getAllUserTasksJson(userId);
	}
	
	@GetMapping("/json-user-closed-tasks-feed")
	public @ResponseBody Map<LocalDate, Integer> getAllUserClosedTasksFeedJson(@RequestParam("id") long userId) {
		return userService.getAllUserClosedTasksFeedJson(userId);
	}
	
	@GetMapping("/json-user-closed-issues-feed")
	public @ResponseBody Map<LocalDate, Integer> getAllUserClosedIssuesFeedJson(@RequestParam("id") long userId) {
		return userService.getAllUserClosedIssuesFeedJson(userId);
	}
	
	@GetMapping("/json-user-issues")
	public @ResponseBody JSONArray getAllUserIssuesJson(@RequestParam("id") long userId) {
		return userService.getAllUserIssuesJson(userId);
	}
	
	

}
