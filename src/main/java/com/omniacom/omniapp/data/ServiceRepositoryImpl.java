package com.omniacom.omniapp.data;

import java.util.Date;
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
		Query query = entityManager.createQuery("SELECT t FROM Task t WHERE t.service=:param").setParameter("param",
				service);
		tasks = (List<Task>) query.getResultList();
		return tasks;
	}

	public Date getServiceClosedDate(Service service) {
		Query query = entityManager.createQuery("SELECT MAX(t.completedOn) FROM Task t WHERE t.completedOn!=null")
				;
		List<Date> dates = (List<Date>) query.getResultList();
		if (!dates.isEmpty())
			return dates.get(0);
		return null;
	}

	@Override
	public List<Service> findAllByPoNumber(long projectId, String poNumber) {
		List<Service> services = null;
		Query query = entityManager.createQuery("SELECT s FROM Service s WHERE s.poNumber LIKE :param AND "
				+ "(s.operation.project.id = :param2 OR s.project.id = :param2)").setParameter("param",
				poNumber).setParameter("param2", projectId);
		services = (List<Service>) query.getResultList();
		return services;
	}

}
