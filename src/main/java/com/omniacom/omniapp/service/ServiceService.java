package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.repository.ServiceRepository;

import net.sf.json.JSONObject;

@Service
public class ServiceService {

	@Autowired
	ServiceRepository serviceRepo;
	
	public com.omniacom.omniapp.entity.Service findById(long id){
		return serviceRepo.findOne(id);
	}
	
	public List<Task> findAllTasks(com.omniacom.omniapp.entity.Service service){
		return serviceRepo.findAllTasks(service);
	}
	
	public JSONObject jsonService(com.omniacom.omniapp.entity.Service service) {
		return new JSONObject()
					.element("id", service.getId())
					.element("name", service.getName())
					.element("category", service.getCategory())
					.element("description", service.getDescription())
					.element("price", service.getPriceHT())
					.element("creationDate", new SimpleDateFormat("dd MM YYYY", Locale.ENGLISH).format(service.getCreationDate()))
					.element("taskCount", serviceRepo.findAllTasks(service).size());
	}
}
