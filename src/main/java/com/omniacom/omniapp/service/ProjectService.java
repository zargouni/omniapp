package com.omniacom.omniapp.service;

import java.util.Date;

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
	

}
