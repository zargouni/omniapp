package com.omniacom.omniapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	ProjectRepository projectRepo;

	public void addProject(Project project) {
		project.setCreationDate(new Date());
		projectRepo.save(project);
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

}
