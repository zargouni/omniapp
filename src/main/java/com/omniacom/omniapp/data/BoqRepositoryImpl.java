package com.omniacom.omniapp.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.BoqService;
import com.omniacom.omniapp.entity.BoqServiceId;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.BoqServiceRepository;
import com.omniacom.omniapp.repository.custom.BoqRepositoryCustom;


@Repository
@Transactional
public class BoqRepositoryImpl implements BoqRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	BoqServiceRepository boqStRepo;

	@Override
	public List<ServiceTemplate> findAllServiceTemplates(BillOfQuantities boq) {
		List<ServiceTemplate> services = null;
		Query query = entityManager.createQuery(
				"SELECT s FROM ServiceTemplate s, BoqService porteuse WHERE porteuse.boq.id = :param AND s.id = porteuse.template.id")
				.setParameter("param", boq.getId());
		services = (List<ServiceTemplate>) query.getResultList();
		return services;
	}

	@Override
	public boolean addAllServiceTemplates(BillOfQuantities boq, List<ServiceTemplate> templates) {
		// BoqService boqService = new BoqService();
		// for(ServiceTemplate template : templates)
		// template.assignBoqServicesToThisTemplate(boqServices);
		// if (boq.getServices() != null) {
		// return boq.getServices().addAll(templates);
		// }
		// boq.setServices(new ArrayList<>());
		// return boq.getServices().addAll(templates);
		return false;

	}

	@Override
	public boolean addOneServiceTemplate(BillOfQuantities boq, ServiceTemplate template,float price) {
		BoqService boqService = new BoqService();
		boqService.getBoqServiceId().setPrice(price);
		boq.assignBoqServiceToThisBoq(boqService);
		template.assignBoqServiceToThisTemplate(boqService);
		if (boqStRepo.save(boqService) != null)
			return true;
		return false;
	}

	@Override
	public boolean removeOneServiceTemplate(BillOfQuantities boq, ServiceTemplate template) {
		BoqServiceId bstId = new BoqServiceId();
		bstId.setBoqId(boq.getId());
		bstId.setTemplateId(template.getId());
		BoqService bs = boqStRepo.findOne(bstId);
		if(bs != null) {
			boqStRepo.delete(bstId);
			return true;
		}
		return false;
	}

	@Override
	public void removeAllServiceTemplates(BillOfQuantities boq) {
		Query query = entityManager.createQuery(
				"DELETE FROM BoqService s WHERE s.boq.id = :param ")
				.setParameter("param", boq.getId());
		int result = query.executeUpdate(); 
	}

	@Override
	public boolean templateExists(BillOfQuantities boq, ServiceTemplate template) {
		Query query = entityManager.createQuery(
				"SELECT bs FROM BoqService bs WHERE bs.boq.id = :param AND bs.template.id = :templateId")
				.setParameter("param", boq.getId()).setParameter("templateId", template.getId());
		if (query.getResultList().size() != 0)
			return true;
		return false;
	}

	@Override
	public boolean boqExists(String name) {
		Query query = entityManager.createQuery("SELECT boq FROM BillOfQuantities boq WHERE boq.name = :param")
				.setParameter("param", name);
		if (query.getResultList().size() != 0)
			return true;
		return false;
	}

	@Override
	public List<BillOfQuantities> findAllAvailableValidBoqs() {
		List<BillOfQuantities> validBoqs = new ArrayList<>();
		Query query = entityManager.createQuery("SELECT boq FROM BillOfQuantities boq WHERE boq.project = NULL");
		List<BillOfQuantities> boqs = (List<BillOfQuantities>) query.getResultList();
		for (BillOfQuantities boq : boqs) {
			if (boq.getEndDate().after(new Date())) {
				validBoqs.add(boq);
			}
		}
		return validBoqs;
	}

	@Override
	public float findServicePriceBoq(BillOfQuantities boq, ServiceTemplate template) {
		Float price = null;
		Query query = entityManager.createQuery(
				"SELECT porteuse.boqServiceId.price FROM BoqService porteuse WHERE porteuse.boq.id = :param AND porteuse.template.id = :param2")
				.setParameter("param", boq.getId())
				.setParameter("param2", template.getId());
		List<Float> results = (List<Float>) query.getResultList();
		if (!results.isEmpty())
			// ignores multiple results
			price = (Float) results.get(0);
		return price;
	}

}
