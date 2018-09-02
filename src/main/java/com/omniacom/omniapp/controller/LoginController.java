package com.omniacom.omniapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;

@Controller
public class LoginController {

	@GetMapping("/login")
	public ModelAndView index(ModelAndView modelAndView) {
		modelAndView.setViewName("login");
		return modelAndView;

	}

}
