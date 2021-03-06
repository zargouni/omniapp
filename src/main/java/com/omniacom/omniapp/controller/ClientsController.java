package com.omniacom.omniapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Nature;
import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.repository.ClientRepository;
import com.omniacom.omniapp.service.ClientService;
import com.omniacom.omniapp.service.FileStorageService;
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

	@Autowired
	FileStorageService fileService;

	@Autowired
	ClientRepository clientRepo;

	@GetMapping("/clients")
	public @ResponseBody ModelAndView index(Model model) {
		return new ModelAndView("clients");
	}

	@GetMapping("/get-all-clients-details-json")
	public @ResponseBody JSONArray getAllClients() {
		return clientService.findAllClientsJson();
	}

	// @GetMapping("/get-client-stats")
	// public @ResponseBody JSONObject getClientStats(@RequestParam("clientId") long
	// clientId) {
	// Client client = clientService.findById(clientId);
	// JSONObject json = new JSONObject().element("projects",
	// clientService.projectsCount(client)).element("money",
	// clientService.moneyCount(client));
	//
	// return json;
	// }

	@GetMapping("/get-client-sites")
	public @ResponseBody JSONArray getClientSites(@RequestParam("clientId") long clientId) {
		Client client = clientService.findById(clientId);
		return clientService.findAllSitesJson(client);
	}

	@PostMapping("/save-new-site")
	public @ResponseBody JsonResponse saveSite(@Validated Site site, @RequestParam("clientId") long clientId,
			BindingResult result) throws IOException {
		Client client = clientService.findById(clientId);
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors() && client != null) {
			site.setClient(client);

			if (!clientService.siteExists(client, site))
				response.setResult(siteService.addSite(site).getId());

			response.setStatus("SUCCESS");

		} else if (result.hasErrors()) {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		} else {
			response.setStatus("FAIL_NO_CLIENT");
		}

		return response;
	}

	@GetMapping("/get-site-details")
	public @ResponseBody JsonResponse getSiteDetails(@RequestParam("siteId") long siteId) {
		Site site = siteService.findSite(siteId);
		JsonResponse response = new JsonResponse();
		if (site != null) {
			response.setStatus("SUCCESS");
			JSONObject jsonSite = new JSONObject().element("id", site.getId()).element("name", site.getName())
					.element("latitude", site.getLatitude()).element("longitude", site.getLongitude());

			List<Nature> allNatures = natureService.findAllNatures();

			JSONArray arr = new JSONArray();
			JSONObject jsonNature;

			for (Nature nature : allNatures) {
				jsonNature = new JSONObject().element("id", nature.getId()).element("name", nature.getName());

				if (site.getNatures().contains(nature))
					jsonNature.accumulate("selected", true);
				else
					jsonNature.accumulate("selected", false);

				arr.add(jsonNature);
			}

			jsonSite.accumulate("natures", arr);
			response.setResult(jsonSite);
		} else {
			response.setStatus("FAIL");
		}
		return response;
	}

	@PostMapping("/add-nature-to-site")
	public @ResponseBody JsonResponse addNatureToSite(@RequestParam("siteId") long siteId,
			@RequestParam("nature") Nature nature) throws IOException {

		JsonResponse response = new JsonResponse();

		Site site = siteService.findSite(siteId);
		if (site != null && nature != null) {
			if (site.getNatures() != null)
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
		// Client client = clientService.findById(clientId);
		return natureService.findAllNaturesJson();
	}

	@PostMapping("/add-new-nature")
	public @ResponseBody JsonResponse saveNewNature(@Validated Nature nature, BindingResult result) throws IOException {
		// Client client = clientService.findById(clientId);
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			// site.setClient(client);
			if (!natureService.natureExists(nature)) {
				natureService.addNature(nature);
				response.setStatus("SUCCESS");
			} else {
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
	public @ResponseBody JsonResponse deleteNature(@RequestParam("natureId") long natureId) {
		JsonResponse response = new JsonResponse();

		if (natureService.deleteNature(natureId)) {

			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			// response.setResult(result.getFieldErrors());
		}
		return response;
	}

	@PostMapping("/delete-site")
	public @ResponseBody JsonResponse deleteSite(@RequestParam("siteId") long siteId) {
		JsonResponse response = new JsonResponse();

		if (siteService.deleteSite(siteId)) {

			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
			// response.setResult(result.getFieldErrors());
		}
		return response;
	}

	@InitBinder("nature")
	protected void setNatureValidator(WebDataBinder binder) {
		binder.addValidators(natureValidator);
	}

	@PostMapping("/update-site-natures")
	public @ResponseBody JsonResponse addNatureToSite(@RequestParam("nature") Nature nature,
			@RequestParam("siteId") long siteId) throws IOException {

		JsonResponse response = new JsonResponse();

		// ServiceTemplate template = stService.findOne(templateId);
		Site site = siteService.findSite(siteId);

		if (nature != null && site != null) {
			if (!siteService.addOneNature(site, nature))
				response.setStatus("FAIL");
		}
		return response;
	}

	@PostMapping("/update-site")
	public @ResponseBody JsonResponse updateSite(@RequestParam("id") long siteId, @Validated Site site,
			BindingResult result) {
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {

			if (siteService.updateSite(siteId, site)) {

				response.setStatus("SUCCESS");
			} else {
				response.setStatus("FAIL");
			}

		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@GetMapping("/get-client-details")
	public @ResponseBody JSONObject getClientDetails(@RequestParam("id") long clientId) {
		JSONObject json = new JSONObject();
		Client client = clientService.findById(clientId);
		if (client != null) {
			json = clientService.jsonClient(client);
		}
		return json;
	}

	@PostMapping("/update-client")
	public @ResponseBody JsonResponse updateClient(@RequestParam("id") long clientId, @Validated Client updatedClient,
			BindingResult result) {
		JsonResponse response = new JsonResponse();

		if (!result.hasErrors()) {
			if (!clientService.clientExists(updatedClient)) {

				if (clientService.updateClient(clientId, updatedClient)) {

					response.setStatus("SUCCESS");
				} else {
					response.setStatus("FAIL");
				}
			} else if (updatedClient.getName().equals(clientService.findById(clientId).getName())) {
				if (clientService.updateClient(clientId, updatedClient)) {

					response.setStatus("SUCCESS");
				} else {
					response.setStatus("FAIL");
				}
			} else {

				response.setStatus("FAIL");
				response.setResult("client-exists");

			}

		} else {
			response.setStatus("FAIL");
			response.setResult(result.getFieldErrors());
		}

		return response;
	}

	@PostMapping(value = "/upload-client-logo")
	public JsonResponse handleClientLogoUpload(@RequestParam("id") long clientId,
			@RequestParam("file") MultipartFile file) {
		boolean success = true;
		JsonResponse response = new JsonResponse();

		Client client = clientService.findById(clientId);
		String logo = fileService.storeLogo(client, file);
		client.setLogo("/logo/" + logo);

		clientRepo.save(client);

		if (success) {
			response.setStatus("SUCCESS");
			return response;
		}
		response.setStatus("FAIL");
		return response;
	}

	@GetMapping("/logo/{fileName:.+}")
	public ResponseEntity<Resource> getClientLogo(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileService.loadLogoAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
