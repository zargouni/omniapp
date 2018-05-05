package com.omniacom.omniapp.repository.custom;

import java.util.List;

import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.entity.TaskTemplate;

public interface ServiceTemplateRepositoryCustom {

	public List<TaskTemplate> findAllTaskTemplates(ServiceTemplate template);

	public boolean addAllTaskTemplates(ServiceTemplate template, List<TaskTemplate> taskTemplates);

	public boolean addOneTaskTemplate(ServiceTemplate template, TaskTemplate taskTemplate);

	public boolean removeOneTaskTemplate(ServiceTemplate template, TaskTemplate taskTemplate);

	public boolean removeAllTaskTemplates(ServiceTemplate template, List<TaskTemplate> taskTemplates);
	
	public boolean serviceExists(String name);
	

}
