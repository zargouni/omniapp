package com.omniacom.omniapp.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.UploadedFile;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.UserRepository;
import com.omniacom.omniapp.service.UploadedFileService;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.JsonResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class ProfileController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UploadedFileService fileService;
	
	@Autowired
	UserRepository userRepo;
	
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
	
	@PostMapping(value = "/upload-profile-picture")
	public JsonResponse handleProfilePictureUpload(@RequestParam("file") MultipartFile file) {
		boolean success = true;
		JsonResponse response = new JsonResponse();
			try {
				User user = userRepo.findOne(userService.getSessionUser().getId()); 
				String picture = fileService.savePicToLocalDisk(user,file);
				user.setProfilePic(picture);
				
				userRepo.save(user);
			
			} catch (IOException e) {
				e.printStackTrace();
				success = false;
			}
			
		if (success) {
			response.setStatus("SUCCESS");
			return response;
		}
		response.setStatus("FAIL");
		return response;

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
