package com.omniacom.omniapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.repository.ServiceTemplateRepository;

@Service
public class ServiceTemplateService {

	@Autowired
	ServiceTemplateRepository serviceTemplateRepo;
	
	public ServiceTemplate addServiceTemplate(ServiceTemplate template) {
		return serviceTemplateRepo.save(template);
	}
	
	public List<ServiceTemplate> findAllServiceTemplates(){
		return (List<ServiceTemplate>) serviceTemplateRepo.findAll();
	}
	
	public boolean deleteServiceTemplate(long serviceId) {
		if(serviceTemplateRepo.exists(serviceId)) {
			serviceTemplateRepo.delete(serviceId);
			return true;
		}
		return false;
	}
	
	public ServiceTemplate findOne(long id) {
		return serviceTemplateRepo.findOne(id);
	}
}
