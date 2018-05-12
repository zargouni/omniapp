package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.repository.ServiceRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ServiceService {

	@Autowired
	ServiceRepository serviceRepo;
	
	@Autowired
	TaskService taskService;
	
	public com.omniacom.omniapp.entity.Service findById(long id){
		return serviceRepo.findOne(id);
	}
	
	public com.omniacom.omniapp.entity.Service addService(com.omniacom.omniapp.entity.Service service) {
		service.setCreationDate(new Date());
		return serviceRepo.save(service);
	}
	
	public List<Task> findAllTasks(com.omniacom.omniapp.entity.Service service){
		return serviceRepo.findAllTasks(service);
	}
	
	
	
	public String getServicePercentageComplete(com.omniacom.omniapp.entity.Service service) {
		List<Task> tasks = serviceRepo.findAllTasks(service);
		Integer complete = 0;
		for(Task t : tasks) {
			if(t.getStatus().equals(StaticString.TASK_STATUS_COMPLETED))
				complete++;
		}
		if(complete != 0)
		return (complete*100/tasks.size()+"%");
		return "0%";
	}
	
	public JSONObject jsonService(com.omniacom.omniapp.entity.Service service) {
		return new JSONObject()
					.element("id", service.getId())
					.element("name", service.getName())
					.element("category", service.getCategory())
					.element("description", service.getDescription())
					.element("price", service.getPriceHT())
					.element("creationDate", new SimpleDateFormat("dd MM YYYY", Locale.ENGLISH).format(service.getCreationDate()))
					.element("taskCount", serviceRepo.findAllTasks(service).size())
					.element("percentage", getServicePercentageComplete(service));
	}

	public JSONArray getAllServiceTasksJson(long serviceId) {
		JSONArray array = new JSONArray();
		com.omniacom.omniapp.entity.Service service = serviceRepo.findOne(serviceId);
		if(service != null && service.getTasks().size() >0) {
			for(Task t : service.getTasks()) {
				array.add(taskService.jsonTaskFormattedDates(t));
			}
		}
		return array;
	}
}
