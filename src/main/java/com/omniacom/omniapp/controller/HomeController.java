package com.omniacom.omniapp.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.service.UserService;

import net.sf.json.JSONArray;

@RestController
public class HomeController {

	@Autowired
	private UserService userService;

	@GetMapping("/home")
	public @ResponseBody ModelAndView index(Model model) {
		model.addAttribute("addProjectFormAction", "home/add-project");
		model.addAttribute("ISSUE_STATUS_OPEN", StaticString.ISSUE_STATUS_OPEN);
		model.addAttribute("ISSUE_STATUS_IN_PROGRESS", StaticString.ISSUE_STATUS_IN_PROGRESS);
		model.addAttribute("ISSUE_STATUS_TO_BE_TESTED", StaticString.ISSUE_STATUS_TO_BE_TESTED);
		model.addAttribute("ISSUE_STATUS_CLOSED", StaticString.ISSUE_STATUS_CLOSED);
		return new ModelAndView("index");
	}

	@ModelAttribute(name = "tasks")
	public List<Task> getAllTasks() {
		List<Task> tasks = userService.findAllTasks(userService.getSessionUser());
		if (tasks.size() >= 5)
			return tasks.subList(0, 5);
		return tasks;
	}

	@ModelAttribute(name = "issues")
	public List<Issue> getAllIssues() {
		List<Issue> issues = userService.findAllIssues(userService.getSessionUser());
		if (issues.size() >= 5)
			return issues.subList(0, 5);
		return issues;
	}

	@ModelAttribute(name = "overdueItems")
	public Collection<Object> getOverdueItems() {
		JSONArray items = userService.findAllOverdueItems(userService.getSessionUser());
		if (items.size() >= 5)
			return items.subList(0, 5);
		return items;
	}

	@ModelAttribute(name = "itemsDueToday")
	public Collection<Object> getItemsDueToday() {
		JSONArray items = userService.findAllItemsDueToday(userService.getSessionUser());
		if (items.size() >= 5)
			return items.subList(0, 5);
		return items;
	}

	@GetMapping("/get-all-user-overdue-items")
	public Collection<Object> getAllOverdueItems() {
		return userService.findAllOverdueItems(userService.getSessionUser());
	}

	@GetMapping("/json-user-overview-feed")
	public @ResponseBody Map<String, Integer> getAllUserClosedTasksheyFeedJson() {
		return userService.getUserOverviewFeedJson(userService.getSessionUser().getId());
	}

	@GetMapping("/get-user-calendar-events")
	public @ResponseBody JSONArray getProjectCalendarEvents() {
		return userService.getCalendarEvents(userService.getSessionUser());
	}

}
