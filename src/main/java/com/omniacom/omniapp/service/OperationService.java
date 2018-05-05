package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.TaskTemplate;
import com.omniacom.omniapp.repository.OperationRepository;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.ServiceRepository;
import com.omniacom.omniapp.repository.TaskRepository;
import com.omniacom.omniapp.repository.TaskTemplateRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

	@Autowired
	ProjectRepository projectRepo;
	
	@Autowired
	ServiceService serviceService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	SiteService siteService;
	
	public Operation addOperation(Operation operation) {
		return operationRepo.save(operation);
	}

	public com.omniacom.omniapp.entity.Service generateServiceFromTemplate(long operationId, long serviceTemplateId) {
		com.omniacom.omniapp.entity.Service returnService = null;
		Operation operation = operationRepo.findOne(operationId);
		ServiceTemplate template = stService.findOne(serviceTemplateId);
		if (operation != null && template != null) {
			com.omniacom.omniapp.entity.Service newService = stService.convertToService(template);
			newService.setOperation(operation);
			newService.setCreationDate(new Date());
			returnService = serviceRepo.save(newService);
			for(TaskTemplate t : template.getTasks()) {
				Task task = new Task();
				task.setName(t.getName());
				task.setPriority(t.getPriority());
				task.setEstimationRH(t.getEstimationHR());
				task.setEstimationTime(t.getEstimationTime());
				task.setStatus(StaticString.TASK_STATUS_ONGOING);
				task.setService(returnService);
				taskRepo.save(task);
			}
			
			return returnService;
		}
		return returnService;
	}
	
	public JSONArray getAllOperationsJson(long projectId) {
		Project project = projectRepo.findOne(projectId);
		JSONArray jsonArray = new JSONArray();
		List<Operation> operations = projectRepo.findAllOperations(project);
		for(Operation op : operations) {
			jsonArray.add(jsonOperationFormattedDates(op));
		}
		return jsonArray;
		
	}
	
	public JSONObject jsonOperationFormattedDates(Operation op) {
		return new JSONObject()
				.element("id", op.getId())
				.element("name", op.getName())
				.element("startDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(op.getStartDate()))
				.element("endDate", new SimpleDateFormat("dd MMMM YYYY", Locale.ENGLISH).format(op.getEndDate()))
				.element("status", op.getEndDate().after(new Date()))
				.element("serviceCount", getOperationServiceCount(op))
				.element("flag", op.getFlag())
				.element("project", projectService.jsonProject(op.getProject()))
				.element("site", siteService.jsonSite(op.getSite()));
	}
	
	public Integer getOperationServiceCount(Operation op) {
		return operationRepo.findAllServices(op).size();
	}
	
	public JSONArray getOperationServices(long operationId) {
		JSONArray jsonArray = new JSONArray();
		Operation op = operationRepo.findOne(operationId);
		if(op != null)
		for(com.omniacom.omniapp.entity.Service s : op.getServices()) {
			jsonArray.add(serviceService.jsonService(s));
		}
		return jsonArray;		
	}
	
	public Operation findOne(long operationId) {
		return operationRepo.findOne(operationId);
	}

	

}
