package com.omniacom.omniapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Nature;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.service.ClientService;
import com.omniacom.omniapp.service.NatureService;
import com.omniacom.omniapp.service.SiteService;
import com.omniacom.omniapp.validator.JsonResponse;
import com.omniacom.omniapp.validator.NatureValidator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class ClientsController {

	@Autowired
	ClientService clientService;
	
	@Autowired
	SiteService siteService;
	
	@Autowired
	NatureService natureService;
	
	@Autowired
	NatureValidator natureValidator;
	
	@GetMapping("/clients")
	public @ResponseBody ModelAndView index(Model model) {
		return new ModelAndView("clients");
	}
	
	@GetMapping("/get-all-clients-details-json")
	public @ResponseBody JSONArray getAllClients() {
		return clientService.findAllClientsJson(); 
	}
	
	@GetMapping("/get-client-stats")
	public @ResponseBody JSONObject getClientStats(@RequestParam("clientId") long clientId) {
		Client client = clientService.findById(clientId);
		JSONObject json = new JSONObject()
				.element("projects", clientService.projectsCount(client))
				.element("money",clientService.moneyCount(client));
				
		return json; 
	}
	
	@GetMapping("/get-client-sites")
	public @ResponseBody JSONArray getClientSites(@RequestParam("clientId") long clientId) {
		Client client = clientService.findById(clientId);
		return clientService.findAllSitesJson(client);
	}
	
	@PostMapping("/save-new-site")
	public @ResponseBody JsonResponse saveSite(@Validated Site site,@RequestParam("clientId") long clientId
			,BindingResult result)
			throws IOException {
		Client client = clientService.findById(clientId);
		JsonResponse response = new JsonResponse();
		
		if (!result.hasErrors() && client != null) {
			site.setClient(client);
			
			if (!clientService.siteExists(client, site))
				response.setResult(siteService.addSite(site).getId());

			response.setStatus("SUCCESS");
		
		} else if(result.hasErrors()) {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}else {
			response.setStatus("FAIL_NO_CLIENT");
		}

		return response;
	}
	
	@PostMapping("/add-nature-to-site")
	public @ResponseBody JsonResponse addNatureToSite(@RequestParam("siteId") long siteId,
			@RequestParam("nature") Nature nature) throws IOException {

		JsonResponse response = new JsonResponse();

		Site site = siteService.findSite(siteId);
		if (site != null && nature != null) {
			if(site.getNatures() != null)
				site.getNatures().add(nature);
			else {
				List<Nature> natures = new ArrayList<>();
				natures.add(nature);
				site.setNatures(natures);
			}
			siteService.addSite(site);
			
			response.setStatus("SUCCESS");
			return response;
		}
		response.setStatus("FAIL");
		return response;
	}
	
	@GetMapping("/json-natures")
	public @ResponseBody JSONArray getNatures() {
		//Client client = clientService.findById(clientId);
		return natureService.findAllNaturesJson();
	}
	
	@PostMapping("/add-new-nature")
	public @ResponseBody JsonResponse saveNewNature(@Validated Nature nature, BindingResult result)
			throws IOException {
		//Client client = clientService.findById(clientId);
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			//site.setClient(client);
			if (!natureService.natureExists(nature)) {
				natureService.addNature(nature);
				response.setStatus("SUCCESS");
			}else {
				response.setStatus("FAIL");
				response.setResult("nature-exists");
			}
		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}
	
	@PostMapping("/delete-nature")
	public @ResponseBody JsonResponse deleteNature(@RequestParam("natureId") long natureId ) {
		JsonResponse response = new JsonResponse();

		if (natureService.deleteNature(natureId)) {
			
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			//response.setResult(result.getFieldErrors());
		}
		return response;
	}
	
	@PostMapping("/delete-site")
	public @ResponseBody JsonResponse deleteSite(@RequestParam("siteId") long siteId ) {
		JsonResponse response = new JsonResponse();

		if (siteService.deleteSite(siteId)) {
			
			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			//response.setResult(result.getFieldErrors());
		}
		return response;
	}
	
	
	@InitBinder("nature")
	protected void setNatureValidator(WebDataBinder binder) {
		binder.addValidators(natureValidator);
	}
}
