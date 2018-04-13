package com.omniacom.omniapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.repository.ServiceRepository;

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
}
