package com.omniacom.omniapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.ServiceRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ProjectService {

	@Autowired
	ProjectRepository projectRepo;

	@Autowired
	ServiceRepository serviceRepo;

	@Autowired
	ServiceService serviceService;
	
	private Project currentProject;
	
	

	public Project addProject(Project project) {
		project.setCreationDate(new Date());
		Project proj = projectRepo.save(project);
		com.omniacom.omniapp.entity.Service generalService = new com.omniacom.omniapp.entity.Service("General",
				project);
		generalService.setCreationDate(new Date());
		serviceRepo.save(generalService);
		return proj;
	}

	public Project findOneById(long id) {
		return projectRepo.findOne(id);
	}

	public List<Integer> findTaskStatusCount(Project project) {
		List<Integer> sizes = new ArrayList<Integer>();
		sizes.add(projectRepo.findAllCompletedTasks(project).size());
		sizes.add(projectRepo.findAllOnGoingTasks(project).size());
		return sizes;
	}

	public Map<com.omniacom.omniapp.entity.Service, List<Task>> getMapServiceTasks(Project project) {
		Map<com.omniacom.omniapp.entity.Service, List<Task>> map = new TreeMap<>();
		for (com.omniacom.omniapp.entity.Service s : projectRepo.findAllServices(project))
			map.put(s, serviceService.findAllTasks(s));
		return map;
	}
	
	public JSONArray getAllOperationsJson(long projectId) {
		JSONArray array = new JSONArray();
		Project project = projectRepo.findOne(projectId);
		if(project != null) {
			for(Operation op : project.getOperations()) {
				array.add(new JSONObject()
						.element("id", op.getId())
						.element("name", op.getName())
						);
			}
		}
		return array;
	}

	public Integer findCompletedTasksCount(Project project) {
		return projectRepo.findAllCompletedTasks(project).size();
	}

	public Integer findOnGoingTasksCount(Project project) {
		return projectRepo.findAllOnGoingTasks(project).size();
	}

	public Integer findTaskCount(Project project) {
		return projectRepo.findAllCompletedTasks(project).size() + projectRepo.findAllOnGoingTasks(project).size();
	}

	public Iterable<com.omniacom.omniapp.entity.Service> findAllServices(Project selectedProject) {
		// TODO Auto-generated method stub
		return projectRepo.findAllServices(selectedProject);
	}
	
	public List<BillOfQuantities> findAllBoqs(Project project){
		return projectRepo.findAllBoqs(project);
	}

	/**
	 * @return the currentProject
	 */
	public Project getCurrentProject() {
		return currentProject;
	}

	/**
	 * @param currentProject the currentProject to set
	 */
	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}
	
	public JSONObject jsonProject(Project project) {
		JSONObject json = new JSONObject()
				.element("id", project.getId())
				.element("name", project.getName())
				.element("client", project.getClient().getName())
				.element("owner", project.getOwner().getFirstName()+" "+project.getOwner().getLastName())
				.element("country", project.getCountry())
				.element("currency", project.getCurrency())
				.element("percentage", getProjectProgress(project));
		return json;
	}
	
	public String getProjectProgress(Project project) {
		Integer taskCount = findTaskCount(project);
		if(taskCount != 0)
		return (findCompletedTasksCount(project)*100 / taskCount)+"%";
		return "0%";
	}

	public JSONObject getProjectTaskStats(long projectId) {
		JSONObject json = new JSONObject();
		Project project = projectRepo.findOne(projectId);
		Integer taskCount = findTaskCount(project);
		
		
		if( taskCount != 0) {
			json
			.element("totalTasks", taskCount)
			.element("ongoing", (findOnGoingTasksCount(project)*100 / taskCount)+"%")
			.element("completed", (findCompletedTasksCount(project)*100 / taskCount)+"%");
		}
		else
			json
			.element("totalTasks", taskCount)
			.element("ongoing", "None")
			.element("completed", "None");
			
		return json;
	}

}
