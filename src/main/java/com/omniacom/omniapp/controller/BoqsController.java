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

import com.omniacom.omniapp.entity.BillOfQuantities;
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
		for(BillOfQuantities boq : boqs) {
			jsonArray.add(boqService.jsonBoq(boq));
		}
		return jsonArray;
	}
	
	@GetMapping("/get-boq-details")
	public @ResponseBody JSONObject getBoqDetails(@RequestParam("id") long id) {
		BillOfQuantities boq = boqService.findOne(id);
		return boqService.jsonBoq(boq)
				.element("services", stService.jsonServiceTemplates(boqService.findAllServiceTemplates(boq)));
	}
	
	@PostMapping("/delete-boq")
	public @ResponseBody JsonResponse deleteBoq(@RequestParam("boqId") long boqId ) {
		JsonResponse response = new JsonResponse();

		if (boqService.deleteBoq(boqId)) {
			
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
		}
		return response;
	}
}
