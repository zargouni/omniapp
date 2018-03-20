package com.omniacom.omniapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
public class AllProjectsController {
	
	@GetMapping("/projects")
	public String index() {
		return "projects";
	}

}
