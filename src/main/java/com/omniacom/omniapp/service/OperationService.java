package com.omniacom.omniapp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.repository.OperationRepository;

@Service
public class OperationService {
	
	@Autowired
	OperationRepository operationRepo;

	public void addOperation(Operation operation) {
		operationRepo.save(operation);
	}


}
