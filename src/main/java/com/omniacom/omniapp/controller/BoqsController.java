package com.omniacom.omniapp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.service.BoqService;
import com.omniacom.omniapp.service.ServiceTemplateService;
import com.omniacom.omniapp.validator.JsonResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class BoqsController {

	@Autowired
	BoqService boqService;

	@Autowired
	ServiceTemplateService stService;

	@GetMapping("/boqs")
	public @ResponseBody ModelAndView index(Model model) {
		return new ModelAndView("boqs");
	}

	@GetMapping("/json-boqs")
	public @ResponseBody JSONArray populateDatatable() {
		JSONArray jsonArray = new JSONArray();
		List<BillOfQuantities> boqs = boqService.findAllBoqs();
		for (BillOfQuantities boq : boqs) {
			jsonArray.add(boqService.jsonBoqFormattedDates(boq));
		}
		return jsonArray;
	}

	@GetMapping("/get-boq-details")
	public @ResponseBody JSONObject getBoqDetails(@RequestParam("id") long id) {
		BillOfQuantities boq = boqService.findOne(id);
		return boqService.jsonBoq(boq).element("services",
				stService.jsonServiceTemplatesWithPrices(boq, boqService.findAllServiceTemplates(boq)));
	}

	@PostMapping("/delete-boq")
	public @ResponseBody JsonResponse deleteBoq(@RequestParam("boqId") long boqId) {
		JsonResponse response = new JsonResponse();

		if (boqService.deleteBoq(boqId)) {

			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
		}
		return response;
	}

	@PostMapping("/update-boq")
	public @ResponseBody JsonResponse updateBoq(@RequestParam("id") long boqId, @Validated BillOfQuantities boq,
			BindingResult result) {
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			if (!boqService.boqNameExists(boq.getName())) {

				if (boqService.updateBoq(boqId, boq)) {

					response.setStatus("SUCCESS");
				} else {
					response.setStatus("FAIL");
				}
			} else if (boq.getName().equals(boqService.findOne(boqId).getName())) {
				if (boqService.updateBoq(boqId, boq)) {

					response.setStatus("SUCCESS");
				} else {
					response.setStatus("FAIL");
				}
			} else {

				response.setStatus("FAIL");
				response.setResult("boq-exists");

			}

		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@PostMapping("/update-boq-service-templates")
	public @ResponseBody JsonResponse addServiceTemplateToBoq(@RequestParam("id") long templateId,
			@RequestParam("boqId") long boqId, @RequestParam("price") String priceString) throws IOException {
		float priceHT = Float.valueOf(priceString);
		JsonResponse response = new JsonResponse();

		ServiceTemplate template = stService.findOne(templateId);
		BillOfQuantities boq = boqService.findOne(boqId);
		if (template != null && boq != null) {
			if (!boqService.addOneServiceTemplate(boq, template, priceHT))
				response.setStatus("FAIL");
		}
		return response;
	}

}
