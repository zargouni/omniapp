package com.omniacom.omniapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.Classification;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.service.ClassificationService;
import com.omniacom.omniapp.validator.ClassificationValidator;
import com.omniacom.omniapp.validator.IssueValidator;
import com.omniacom.omniapp.validator.JsonResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class ProblemsController {
	
	@Autowired
	ClassificationValidator classificationValidator;
	
	@Autowired
	ClassificationService classService;
	
	@GetMapping("/problem-manager")
	public @ResponseBody ModelAndView index(Model model) {
		return new ModelAndView("problems");
	}
	
	@GetMapping("/json-classification")
	public @ResponseBody JSONObject getClassification(@RequestParam("classId") long classId) {
		return classService.jsonClassification(classService.findById(classId));
	}

	@GetMapping("/json-classifications")
	public @ResponseBody JSONArray getAllClassifications() {
		return classService.getAllClassifications();
	}
	
	@PostMapping("/add-classification")
	public @ResponseBody JsonResponse addClassification(@Validated Classification c, BindingResult result) {
		JsonResponse response = new JsonResponse();
		if (!result.hasErrors()) {
			if(classService.addClassification(c)) {
				response.setStatus("SUCCESS");
		}}else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}
		return response;
	}
	
	@PostMapping("/update-classification")
	public @ResponseBody JsonResponse updateClassification(@RequestParam("classId") long classId, @Validated Classification c, BindingResult result) {
		JsonResponse response = new JsonResponse();
		if (!result.hasErrors()) {
			Classification classification = classService.findById(classId);
			if(classService.updateClassification(classification,c)) {
				response.setStatus("SUCCESS");
		}}else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}
		return response;
	}
	
	@PostMapping("/delete-classification")
	public @ResponseBody JsonResponse deleteClassification(@RequestParam("classId") long classId) {
		JsonResponse response = new JsonResponse();
		Classification classification = classService.findById(classId);
		if(classification != null) {
			classService.delete(classification);
			response.setStatus("SUCCESS");
		}else {
			response.setStatus("FAIL");
		}
		return response;
	}

	@InitBinder("classification")
	protected void setClassificationValidator(WebDataBinder binder) {
		binder.addValidators(classificationValidator);
	}
}
