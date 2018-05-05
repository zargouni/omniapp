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
import com.omniacom.omniapp.validator.TaskTemplateValidator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class TemplatingController {

	@Autowired
	ServiceTemplateService stService;
	
	@Autowired
	private ServiceTemplateValidator serviceTemplateValidator;
	
	@Autowired
	private TaskTemplateValidator taskTemplateValidator;
	
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
			jsonArray.add(stService.jsonServiceTemplate(template));
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
	public @ResponseBody JsonResponse saveTask(@ModelAttribute("taskTemplate") @Validated TaskTemplate taskTemplate, @RequestParam("serviceId") long serviceId, BindingResult result)
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
	
	@GetMapping("/get-service-template-details")
	public @ResponseBody JSONObject getServiceTemplateDetails(@RequestParam("id") long templateId) {
		ServiceTemplate st = stService.findOne(templateId);
		return stService.jsonServiceTemplate(st);
	}
	
	@PostMapping("/update-service-template-details")
	public JsonResponse doUpdateServiceTemplateDetails(@RequestParam("id") long templateId
			, @Validated ServiceTemplate updatedTemplate
			, BindingResult result) {
		
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			if (!stService.serviceNameExists(updatedTemplate.getName())) {
				
				if (stService.updateService(templateId, updatedTemplate)) {

					response.setStatus("SUCCESS");
				} else {
					response.setStatus("FAIL");
				}
			} else if (updatedTemplate.getName().equals(stService.findOne(templateId).getName())) {
				if (stService.updateService(templateId, updatedTemplate)) {

					response.setStatus("SUCCESS");
				} else {
					response.setStatus("FAIL");
				}
			} else {

				response.setStatus("FAIL");
				response.setResult("template-exists");

			}

		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}
	
	
	@InitBinder("serviceTemplate")
	protected void setServiceTemplateValidator(WebDataBinder binder) {
		binder.addValidators(serviceTemplateValidator);
	}
	
	@InitBinder("taskTemplate")
	protected void setTaskTemplateValidator(WebDataBinder binder) {
		binder.addValidators(taskTemplateValidator);
	}
}
