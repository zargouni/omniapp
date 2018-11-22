package com.omniacom.omniapp.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ProblemsController {
	
	@GetMapping("/problem-manager")
	public @ResponseBody ModelAndView index(Model model) {
		return new ModelAndView("problems");
	}

}
