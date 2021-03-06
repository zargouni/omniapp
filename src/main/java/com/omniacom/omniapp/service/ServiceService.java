package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.StaticString;
import com.omniacom.omniapp.entity.LogChange;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.UpdateLog;
import com.omniacom.omniapp.repository.ServiceRepository;
import com.omniacom.omniapp.repository.UpdateLogRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ServiceService {

	@Autowired
	ServiceRepository serviceRepo;

	@Autowired
	TaskService taskService;

	@Autowired
	OperationService operationService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UpdateLogRepository updateLogRepo;

	public com.omniacom.omniapp.entity.Service findById(long id) {
		return serviceRepo.findOne(id);
	}

	public com.omniacom.omniapp.entity.Service addService(com.omniacom.omniapp.entity.Service service) {
		service.setCreationDate(new Date());
		return serviceRepo.save(service);
	}

	public List<Task> findAllTasks(com.omniacom.omniapp.entity.Service service) {
		return serviceRepo.findAllTasks(service);
	}

	public String getServicePercentageComplete(com.omniacom.omniapp.entity.Service service) {
		List<Task> tasks = serviceRepo.findAllTasks(service);
		Integer percentage = 0;
		for (Task t : tasks) {
			percentage += Integer.valueOf(t.getCompletionPercentage());
			
		}
		if (tasks.size() != 0)
			return (percentage / tasks.size()+"");
		return "0";
	}

	public String getServiceOpenTasksPercentage(com.omniacom.omniapp.entity.Service service) {
		List<Task> tasks = serviceRepo.findAllTasks(service);
		Integer open = 0;
		for (Task t : tasks) {
			if (t.getStatus().equals(StaticString.TASK_STATUS_ONGOING))
				open++;
		}
		if (tasks.size() != 0)
			return (open * 100 / tasks.size() + "%");
		return "0%";
	}

	public JSONObject jsonService(com.omniacom.omniapp.entity.Service service) {
		JSONObject jsonService = new JSONObject().element("id", service.getId()).element("name", service.getName())
				.element("category", service.getCategory()).element("description", service.getDescription())
				.element("price", service.getPriceHT())
				.element("flag", service.getFlag())
				.element("creationDate",
						// new SimpleDateFormat("dd MM YYYY",
						// Locale.ENGLISH).format(service.getCreationDate()))
						service.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
				.element("formattedCreationDate",
						new SimpleDateFormat("dd MMMM YYYY - hh:mm", Locale.ENGLISH).format(service.getCreationDate()))
				.element("creationTime",
						new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(service.getCreationDate()))

				.element("taskCount", serviceRepo.findAllTasks(service).size())
				.element("percentage", getServicePercentageComplete(service))
				// .element("tasks", getAllServiceTasksGantt(service.getId()))
				.element("openTasksPercentage", getServiceOpenTasksPercentage(service));
		if (getServiceClosedDate(service) != null)
			jsonService.accumulate("closedDate",
					new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(getServiceClosedDate(service)));
		else
			jsonService.accumulate("closedDate", "open");

		if (service.getOperation() != null) {
			jsonService.accumulate("parent", operationService.jsonOperationFormattedDates(service.getOperation()));
			jsonService.accumulate("currency", service.getOperation().getProject().getCurrency());

		} else {
			jsonService.accumulate("parent", "none");
			jsonService.accumulate("currency", service.getProject().getCurrency());
		}
		
		if(service.isDeleted())
			jsonService.accumulate("deletionDate", new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH).format(service.getDeletionDate()))
				.accumulate("deletionTime",new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(service.getDeletionDate()));

		return jsonService;

	}

	public JSONArray getAllServiceTasksJson(long serviceId) {
		JSONArray array = new JSONArray();
		com.omniacom.omniapp.entity.Service service = serviceRepo.findOne(serviceId);
		if (service != null && service.getTasks().size() > 0) {
			for (Task t : service.getTasks()) {
				array.add(taskService.jsonTaskFormattedDates(t));
			}
		}
		return array;
	}

	public JSONArray getAllServiceTasksGantt(long serviceId) {
		JSONArray array = new JSONArray();
		com.omniacom.omniapp.entity.Service service = serviceRepo.findOne(serviceId);
		if (service != null && service.getTasks().size() > 0) {
			for (Task t : service.getTasks()) {
				array.add(taskService.jsonTaskForGantt(t));
			}
		}
		return array;
	}

	public Date getServiceClosedDate(com.omniacom.omniapp.entity.Service service) {
		if (getServicePercentageComplete(service).equals("100%"))
			return serviceRepo.getServiceClosedDate(service);
		return null;

	}

	public Date getServiceDates(com.omniacom.omniapp.entity.Service service) {
		List<Task> tasks = findAllTasks(service);
		List<Date> dateList = new ArrayList<Date>();
		if (!tasks.isEmpty()) {
			for (Task t : tasks) {
				dateList.add(t.getEndDate());
				// dateList.add(t.getEndDate());
			}
			dateList.sort(new Comparator<Date>() {

				@Override
				public int compare(Date o1, Date o2) {
					// TODO Auto-generated method stub
					if (o1.before(o2))
						return -1;
					return 0;
				}
			});

			// dateList = new ArrayList<Date>(dates);
			// Date startDate = (Date) dates.toArray()[0];
			// Date endDate = (Date) dates.toArray()[dates.size()-1];
			// dateList.add((Date) dates.toArray()[0]);
			// dateList.add((Date) dates.toArray()[dates.size()-1]);
			return dateList.get(0);
		}

		return service.getCreationDate();
	}

	public List<com.omniacom.omniapp.entity.Service> findAllByPoNumber(long projectId, String poNumber) {
		return serviceRepo.findAllByPoNumber(projectId, poNumber);
	}

	public com.omniacom.omniapp.entity.Service save(com.omniacom.omniapp.entity.Service service) {
		return serviceRepo.save(service);
	}

	public List<com.omniacom.omniapp.entity.Service> findAllSyncedServices() {
		return serviceRepo.findAllSyncedServices();
	}

	public List<Task> findAllUnsyncedTasks(com.omniacom.omniapp.entity.Service s) {
		return serviceRepo.findAllUnsyncedTasks(s);
	}

	public boolean deleteService(long serviceId) {
		if (serviceRepo.exists(serviceId)) {
			com.omniacom.omniapp.entity.Service service = serviceRepo.findOne(serviceId);
			service.setDeletedBy(userService.getSessionUser());
			serviceRepo.save(service);
			serviceRepo.delete(service);
			return true;
		}
		return false;
	}

	public void updateService(com.omniacom.omniapp.entity.Service oldVersionService,
			com.omniacom.omniapp.entity.Service updatedService) {
List<LogChange> changesDone = new ArrayList<LogChange>();
		
		if (!oldVersionService.getName().equals(updatedService.getName())) {
			changesDone.add(new LogChange("name", oldVersionService.getName(), updatedService.getName()));
			oldVersionService.setName(updatedService.getName());		
		}

		if (oldVersionService.getPriceHT() != updatedService.getPriceHT()) {
			changesDone.add(new LogChange("price", String.valueOf(oldVersionService.getPriceHT()), String.valueOf(updatedService.getPriceHT())));
			oldVersionService.setPriceHT(updatedService.getPriceHT());
		}
		
		if (!oldVersionService.getCategory().equals(updatedService.getCategory())) {
			changesDone.add(new LogChange("category", oldVersionService.getCategory().toString(), updatedService.getCategory().toString()));
			oldVersionService.setCategory(updatedService.getCategory());		
		}

		if (oldVersionService.getFlag() != updatedService.getFlag()) {
			changesDone.add(new LogChange("flag", String.valueOf(oldVersionService.getFlag()), String.valueOf(updatedService.getFlag())));
			oldVersionService.setFlag(updatedService.getFlag());
		}
		
		if (!oldVersionService.getOperation().equals(updatedService.getOperation())) {
			changesDone.add(new LogChange("operation", oldVersionService.getOperation().getName(), updatedService.getOperation().getName()));
			oldVersionService.setOperation(updatedService.getOperation());
		}
		

		
		
		UpdateLog updateAction = new UpdateLog();
		updateAction.setActor(userService.getSessionUser());
		updateAction.setDate(new Date());
		updateAction.setService(oldVersionService);
		
		for(LogChange change : changesDone) {
			updateAction.addChange(change);
		}
		
		updateLogRepo.save(updateAction);
		
		oldVersionService.addUpdate(updateAction);
		

		serviceRepo.save(oldVersionService);

		
	}

}
