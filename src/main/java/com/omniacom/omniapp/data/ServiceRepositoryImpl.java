package com.omniacom.omniapp.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.repository.custom.ServiceRepositoryCustom;

public class ServiceRepositoryImpl implements ServiceRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public List<Task> findAllTasks(Service service) {
		List<Task> tasks = null;
		Query query = entityManager.createQuery("SELECT t FROM Task t WHERE t.service=:param")
				.setParameter("param", service);
		tasks = (List<Task>) query.getResultList();
		return tasks;
	}

}
