package com.omniacom.omniapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewProjectController {
	
	@GetMapping("/new_project")
	public String landing() {
		return "new-project";
	}
}
