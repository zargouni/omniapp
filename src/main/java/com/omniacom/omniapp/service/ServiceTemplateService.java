package com.omniacom.omniapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.repository.ServiceTemplateRepository;

import net.sf.json.JSONObject;

@Service
public class ServiceTemplateService {

	@Autowired
	ServiceTemplateRepository serviceTemplateRepo;

	public ServiceTemplate addServiceTemplate(ServiceTemplate template) {
		return serviceTemplateRepo.save(template);
	}

	public List<ServiceTemplate> findAllServiceTemplates() {
		return (List<ServiceTemplate>) serviceTemplateRepo.findAll();
	}

	public boolean deleteServiceTemplate(long serviceId) {
		if (serviceTemplateRepo.exists(serviceId)) {
			serviceTemplateRepo.delete(serviceId);
			return true;
		}
		return false;
	}

	public ServiceTemplate findOne(long id) {
		return serviceTemplateRepo.findOne(id);
	}

	public JSONObject jsonServiceTemplate(ServiceTemplate template) {
		JSONObject jsonService = new JSONObject().element("id", template.getId()).element("name", template.getName())
				.element("description", template.getDescription()).element("price", template.getPrice());
		return jsonService;
	}

	public List<JSONObject> jsonServiceTemplates(List<ServiceTemplate> templates) {
		List<JSONObject> jsonTemplates = new ArrayList<>();
		JSONObject jsonService = null;
		for (ServiceTemplate template : templates) {
			jsonService = new JSONObject().element("id", template.getId())
					.element("name", template.getName()).element("description", template.getDescription())
					.element("price", template.getPrice());
			jsonTemplates.add(jsonService);
		}
		return jsonTemplates;
	}
}
