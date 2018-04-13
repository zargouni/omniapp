package com.omniacom.omniapp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.entity.TaskTemplate;
import com.omniacom.omniapp.repository.TaskTemplateRepository;
import com.omniacom.omniapp.service.ServiceTemplateService;
import com.omniacom.omniapp.validator.JsonResponse;
import com.omniacom.omniapp.validator.ServiceTemplateValidator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class TemplatingController {

	@Autowired
	ServiceTemplateService stService;
	
	@Autowired
	private ServiceTemplateValidator serviceTemplateValidator;
	
	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("serviceTemplate", new ServiceTemplate());
		model.addAttribute("taskTemplate", new TaskTemplate());
		model.addAttribute("serviceTemplates", stService.findAllServiceTemplates());
	}
	
	@GetMapping("/service-templates")
	public @ResponseBody ModelAndView index(Model model) {
		return new ModelAndView("service-templates");
	}
	
	@GetMapping("/json-service-templates")
	public @ResponseBody JSONArray populateDatatable() {
		JSONArray jsonArray = new JSONArray();
		List<ServiceTemplate> services = stService.findAllServiceTemplates();
		for(ServiceTemplate template : services) {
			jsonArray.add(jsonServiceTemplate(template));
		}
		return jsonArray;
	}

	@PostMapping("/delete-service-template")
	public @ResponseBody JsonResponse deleteServiceTemplate(@RequestParam("serviceId") long serviceId ) {
		JsonResponse response = new JsonResponse();

		if (stService.deleteServiceTemplate(serviceId)) {
			
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			//response.setResult(result.getFieldErrors());
		}
		return response;
	}
	
	@PostMapping("/add-service-template")
	public @ResponseBody JsonResponse addServiceTemplate(
			@ModelAttribute("serviceTemplate") @Validated ServiceTemplate template, BindingResult result)
			throws IOException {

		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			stService.addServiceTemplate(template);
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}
	
	@Autowired
	TaskTemplateRepository taskTemplateRepo;
	@PostMapping("/new-service-template-save-task")
	public @ResponseBody JsonResponse saveTask(TaskTemplate taskTemplate, @RequestParam("serviceId") long serviceId, BindingResult result)
			throws IOException {

		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			taskTemplate.setServiceTemplate(stService.findOne(serviceId));
			taskTemplateRepo.save(taskTemplate);

			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}
	
	public JSONObject jsonServiceTemplate(ServiceTemplate template) {
		JSONObject jsonService = new JSONObject()
				.element("id", template.getId())
				.element("name", template.getName())
				.element("description", template.getDescription())
				.element("price", template.getPrice());
		return jsonService;
	}
	
	@InitBinder("serviceTemplate")
	protected void setServiceTemplateValidator(WebDataBinder binder) {
		binder.addValidators(serviceTemplateValidator);
	}
}
