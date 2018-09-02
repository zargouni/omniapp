package com.omniacom.omniapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omniacom.omniapp.service.ProjectService;

import net.sf.json.JSONArray;

@Controller
public class ReportsController {

	@Autowired
	ProjectService projectService;

	@GetMapping(value = "/reports-data", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JSONArray getProjectDetails(@RequestParam("projectID") long projectId) {
		return projectService.getProjectDetailsForReports(projectId);
	}
}
