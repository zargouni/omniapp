package com.omniacom.omniapp.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.entity.TaskTemplate;
import com.omniacom.omniapp.repository.custom.ServiceTemplateRepositoryCustom;

@Repository
@Transactional
public class ServiceTemplateRepositoryImpl implements ServiceTemplateRepositoryCustom{

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public List<TaskTemplate> findAllTaskTemplates(ServiceTemplate template) {
		List<TaskTemplate> tasks = null;
		Query query = entityManager.createQuery(
				"SELECT t FROM TaskTemplate t WHERE t.serviceTemplate.id = :param")
				.setParameter("param", template.getId());
		tasks = (List<TaskTemplate>) query.getResultList();
		return tasks;
	}

	@Override
	public boolean addAllTaskTemplates(ServiceTemplate template, List<TaskTemplate> taskTemplates) {
		if (template.getTasks() != null) {
			return template.getTasks().addAll(taskTemplates);
		}
		template.setTasks(new ArrayList<>());
		return template.getTasks().addAll(taskTemplates);
	}

	@Override
	public boolean addOneTaskTemplate(ServiceTemplate template, TaskTemplate taskTemplate) {
		if (template.getTasks() != null) {
			return template.getTasks().add(taskTemplate);
		}
		template.setTasks(new ArrayList<>());
		return template.getTasks().add(taskTemplate);
	}

	@Override
	public boolean removeOneTaskTemplate(ServiceTemplate template, TaskTemplate taskTemplate) {
		return template.getTasks().remove(taskTemplate);
	}

	@Override
	public boolean removeAllTaskTemplates(ServiceTemplate template, List<TaskTemplate> taskTemplates) {
		return template.getTasks().removeAll(taskTemplates);
	}

}
