package com.omniacom.omniapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.repository.ProjectRepository;
import com.omniacom.omniapp.repository.ServiceRepository;

@Service
public class ProjectService {

	@Autowired
	ProjectRepository projectRepo;
	
	@Autowired
	ServiceRepository serviceRepo;

	public void addProject(Project project) {
		project.setCreationDate(new Date());
		projectRepo.save(project);
		com.omniacom.omniapp.entity.Service generalService = new com.omniacom.omniapp.entity.Service("General",project);
		serviceRepo.save(generalService);
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
	
	public Integer findCompletedTasksCount(Project project) {
		return projectRepo.findAllCompletedTasks(project).size();
	}
	
	public Integer findOnGoingTasksCount(Project project) {
		return projectRepo.findAllOnGoingTasks(project).size();
	}
	
	public Integer findTaskCount(Project project) {
		return projectRepo.findAllCompletedTasks(project).size()+ projectRepo.findAllOnGoingTasks(project).size();
	}

	public Iterable<com.omniacom.omniapp.entity.Service> findAllServices(Project selectedProject) {
		// TODO Auto-generated method stub
		return projectRepo.findAllServices(selectedProject);
	}

}
