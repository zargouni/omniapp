package com.omniacom.omniapp.service;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.TaskTemplate;
import com.omniacom.omniapp.repository.OperationRepository;
import com.omniacom.omniapp.repository.ServiceRepository;
import com.omniacom.omniapp.repository.ServiceTemplateRepository;
import com.omniacom.omniapp.repository.TaskRepository;
import com.omniacom.omniapp.repository.TaskTemplateRepository;

@Service
public class OperationService {
	
	@Autowired
	OperationRepository operationRepo;
	
	@Autowired
	ServiceTemplateService stService;
	
	@Autowired
	ServiceRepository serviceRepo;
	
	@Autowired
	TaskRepository taskRepo;
	
	@Autowired
	TaskTemplateRepository taskTemplateRepo;

	public Operation addOperation(Operation operation) {
		return operationRepo.save(operation);
	}
	
	public com.omniacom.omniapp.entity.Service generateServiceFromTemplate(long operationId, long serviceTemplateId) {
		com.omniacom.omniapp.entity.Service returnService = null;
		Operation operation = operationRepo.findOne(operationId);
		ServiceTemplate template = stService.findOne(serviceTemplateId);
		if(operation != null && template != null) {
			com.omniacom.omniapp.entity.Service newService = stService.convertToService(template);
			newService.setOperation(operation);
			newService.setCreationDate(new Date());
			returnService = serviceRepo.save(newService);
//			for(TaskTemplate taskTemplate : stService.findAllTaskTemplates(template)) {
//				Task task = new Task();
//				task.setName(taskTemplate.getName());
//				task.setEstimationTime(taskTemplate.getEstimationTime());
//				task.setEstimationRH(taskTemplate.getEstimationHR());
//				task.setStatus(StaticString.TASK_STATUS_ONGOING);
//				task.setService(newService);
//				taskRepo.save(task);
//			}
			return returnService;
		}
		return returnService;
	}

	public Task generateServiceTaskFromTemplate(long serviceId, long taskTemplateId, String taskPriority) {
		com.omniacom.omniapp.entity.Service service = serviceRepo.findOne(serviceId);
		TaskTemplate taskTemplate = taskTemplateRepo.findOne(taskTemplateId);
		if(taskTemplate != null && service != null) {
			Task task = new Task();
			task.setName(taskTemplate.getName());
			task.setEstimationTime(taskTemplate.getEstimationTime());
			task.setEstimationRH(taskTemplate.getEstimationHR());
			task.setStatus(StaticString.TASK_STATUS_ONGOING);
			task.setService(service);
			task.setPriority(taskPriority);
			return taskRepo.save(task);
		}
		return null;
	}


}
