package com.omniacom.omniapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.repository.ServiceRepository;

@Service
public class ServiceService {

	@Autowired
	ServiceRepository serviceRepo;
	
	public com.omniacom.omniapp.entity.Service findById(long id){
		return serviceRepo.findOne(id);
	}
}
