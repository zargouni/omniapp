package com.omniacom.omniapp.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.UserRepository;
import com.omniacom.omniapp.service.FileStorageService;
import com.omniacom.omniapp.service.UserService;
import com.omniacom.omniapp.validator.JsonResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class ProfileController {

	@Autowired
	private UserService userService;

	@Autowired
	private FileStorageService fileService;

	@Autowired
	UserRepository userRepo;

	@GetMapping("/profile")
	public ModelAndView index(@RequestParam("id") long userId, Model model) {
		if (userService.findById(userId) == null)
			return new ModelAndView("404");
		return new ModelAndView("profile");
	}

	@ModelAttribute
	public void addAttributes(Model model, @RequestParam("id") long userId) {
		model.addAttribute("profileUser", userService.findById(userId));
	}

	@GetMapping("/get-user-infos")
	public JSONObject getUserInfos(@RequestParam("id") long userId) {
		User sessionUser = userService.getSessionUser();
		if (sessionUser.getId() == userId) {
			return userService.jsonUser(sessionUser);
		}
		return null;
	}

	@PostMapping("/update-user-infos")
	public JsonResponse updateUserInfos(@RequestParam("id") long userId, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("email") String email) {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		JsonResponse response = new JsonResponse();
		if (userService.getSessionUser().getId() == userId) {
			if (userService.updateSessionUserInfos(user)) {
				response.setStatus("SUCCESS");
				return response;
			}
		}
		response.setStatus("FAIL");
		return response;
	}

	@PostMapping("/update-user-password")
	public JsonResponse updateUserPassword(@RequestParam("id") long userId,
			@RequestParam("current") String currentPassword, @RequestParam("new") String newPassword) {
		JsonResponse response = new JsonResponse();
		if (userService.getSessionUser().getId() == userId) {
			if (!userService.getSessionUser().getPassword().equals(currentPassword)) {
				response.setStatus("WRONG-PASS");
			} else if (userService.updateSessionUserPassword(currentPassword, newPassword)) {
				response.setStatus("SUCCESS");
			}

		} else {
			response.setStatus("FAIL");
		}
		return response;
	}

	@PostMapping(value = "/upload-profile-picture")
	public JsonResponse handleProfilePictureUpload(@RequestParam("file") MultipartFile file) {
		boolean success = true;
		JsonResponse response = new JsonResponse();
		User user = userRepo.findOne(userService.getSessionUser().getId());
		String picture = fileService.storePic(user, file);
		user.setProfilePic("/pic/" + picture);

		userRepo.save(user);

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
