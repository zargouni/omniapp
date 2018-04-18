package com.omniacom.omniapp.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.repository.custom.BoqRepositoryCustom;

@Repository
@Transactional
public class BoqRepositoryImpl implements BoqRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<ServiceTemplate> findAllServiceTemplates(BillOfQuantities boq) {
		List<ServiceTemplate> services = null;
		Query query = entityManager.createQuery(
				"SELECT s FROM ServiceTemplate s, BillOfQuantities boq JOIN boq.services services WHERE boq.id = :param AND services.id = s.id")
				.setParameter("param", boq.getId());
		services = (List<ServiceTemplate>) query.getResultList();
		return services;
	}

	@Override
	public boolean addAllServiceTemplates(BillOfQuantities boq, List<ServiceTemplate> templates) {
		if (boq.getServices() != null) {
			return boq.getServices().addAll(templates);
		}
		boq.setServices(new ArrayList<>());
		return boq.getServices().addAll(templates);

	}

	@Override
	public boolean addOneServiceTemplate(BillOfQuantities boq, ServiceTemplate template) {
		if (!templateExists(boq, template)) {
			if (boq.getServices() != null) {
				return boq.getServices().add(template);
			}
			boq.setServices(new ArrayList<>());
			return boq.getServices().add(template);
		}
		return false;

	}

	@Override
	public boolean removeOneServiceTemplate(BillOfQuantities boq, ServiceTemplate template) {
		return boq.getServices().remove(template);
	}

	@Override
	public void removeAllServiceTemplates(BillOfQuantities boq) {
		 boq.getServices().clear();
	}

	@Override
	public boolean templateExists(BillOfQuantities boq, ServiceTemplate template) {
		Query query = entityManager.createQuery(
				"SELECT s FROM ServiceTemplate s, BillOfQuantities boq JOIN boq.services services WHERE boq.id = :param AND services.id = :templateId")
				.setParameter("param", boq.getId()).setParameter("templateId", template.getId());
		if (query.getResultList().size() != 0)
			return true;
		return false;
	}

	@Override
	public boolean boqExists(String name) {
		Query query = entityManager.createQuery("SELECT boq FROM BillOfQuantities boq WHERE boq.name = :param")
				.setParameter("param", name);
		if(query.getResultList().size() != 0)
			return true;
		return false;
	}



}
