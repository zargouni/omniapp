package com.omniacom.omniapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.entity.TaskTemplate;
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
	
	public List<TaskTemplate> findAllTaskTemplates(ServiceTemplate template){
		return serviceTemplateRepo.findAllTaskTemplates(template);
	}

	public com.omniacom.omniapp.entity.Service convertToService(ServiceTemplate template){
		com.omniacom.omniapp.entity.Service service = new com.omniacom.omniapp.entity.Service();
		service.setName(template.getName());
		service.setDescription(template.getDescription());
//		service.setPriceHT(template.getPrice());
		service.setCategory(template.getCategory());
		return service;
	}
	
	public JSONObject jsonServiceTemplate(ServiceTemplate template) {
		JSONObject jsonService = new JSONObject().element("id", template.getId()).element("name", template.getName())
				.element("description", template.getDescription())
//				.element("price", template.getPrice())
				.element("category", template.getCategory());
		return jsonService;
	}

	public List<JSONObject> jsonServiceTemplates(List<ServiceTemplate> templates) {
		List<JSONObject> jsonTemplates = new ArrayList<>();
		JSONObject jsonService = null;
		for (ServiceTemplate template : templates) {
			jsonService = new JSONObject().element("id", template.getId())
					.element("name", template.getName()).element("description", template.getDescription())
//					.element("price", template.getPrice())
					.element("category", template.getCategory());
			jsonTemplates.add(jsonService);
		}
		return jsonTemplates;
	}
	
	public boolean serviceNameExists(String name) {
		return serviceTemplateRepo.serviceExists(name);
	}

	public boolean updateService(long templateId, ServiceTemplate updatedTemplate) {
		ServiceTemplate st = serviceTemplateRepo.findOne(templateId);
		if (st != null) {
			if (!st.getName().equals(updatedTemplate.getName()))
				st.setName(updatedTemplate.getName());
			if (!st.getDescription().equals(updatedTemplate.getDescription()))
				st.setDescription(updatedTemplate.getDescription());
//			if (!(st.getPrice() == updatedTemplate.getPrice()))
//				st.setPrice(updatedTemplate.getPrice());
			if (!(st.getCategory() == updatedTemplate.getCategory()))
				st.setCategory(updatedTemplate.getCategory());
			serviceTemplateRepo.save(st);
			return true;
		}
		return false;
	}
	

}
