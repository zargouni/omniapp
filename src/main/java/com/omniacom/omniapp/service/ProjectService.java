package com.omniacom.omniapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.ServiceRepository;

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
		Map<com.omniacom.omniapp.entity.Service, List<Task>> map = new HashMap<>();
		for (com.omniacom.omniapp.entity.Service s : projectRepo.findAllServices(project))
			map.put(s, serviceRepo.findAllTasks(s));
		return map;
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

}
