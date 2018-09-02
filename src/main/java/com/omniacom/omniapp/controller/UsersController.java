package com.omniacom.omniapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.Role;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.RoleRepository;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.JsonResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class UsersController {

	@Autowired
	UserService userService;
	
	@Autowired
	RoleRepository roleRepo;
	
	@GetMapping("/users")
	public @ResponseBody ModelAndView index(Model model) {
		return new ModelAndView("users");
	}
	
	@GetMapping("/get-all-app-users")
	public @ResponseBody JSONArray getAllUsers() {
		return userService.findAllUsersDetailed();
	}
	
	@GetMapping("/json-roles")
	public @ResponseBody JSONArray getAllUserRoles() {
		JSONArray json = new JSONArray(); 
		List<Role> roles = (List<Role>) roleRepo.findAll();
		 for(Role role : roles) {
			 json.add(new JSONObject().element("id", role.getId())
					 .element("name", role.getName()));
		 }
		 return json;
	}
	
	@PostMapping("/activate-user-account")
	public @ResponseBody JsonResponse activateUserAccount(@RequestParam("userId") long userId) {
		User user = userService.findById(userId);
		JsonResponse response = new JsonResponse();
		if(userService.getSessionUser().getRole().getName().equals("ADMIN")) {
			userService.activateUserAccount(user);
			response.setStatus("SUCCESS");
			return response;
		}
		response.setStatus("FAIL");
		return response;
	}
	
	@PostMapping("/update-user-role")
	public @ResponseBody JsonResponse updateUserRole(@RequestParam("userId") long userId, @RequestParam("role") String roleName) {
		JsonResponse response = new JsonResponse();
		User user = userService.findById(userId);
		Role role = roleRepo.findByName(roleName);
		if(role != null && userService.getSessionUser().getRole().getName().equals("ADMIN")) {
			userService.updateUserRole(user,role);
			response.setStatus("SUCCESS");
			return response;
		}
		response.setStatus("FAIL");
		return response;
	}
}
