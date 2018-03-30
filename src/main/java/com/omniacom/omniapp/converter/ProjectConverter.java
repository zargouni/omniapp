package com.omniacom.omniapp.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.service.ProjectService;


@Component
public class ProjectConverter implements Converter<String, Project>{

	@Autowired
	ProjectService projectService;
	
	@Override
	public Project convert(String id) {
		try {
            Long projectId = Long.valueOf(id);
            return projectService.findOneById(projectId);
        } catch (NumberFormatException e) {
            return null;
        }
	}

}
