package com.omniacom.omniapp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.repository.BoqRepository;

import net.sf.json.JSONObject;

@Service
public class BoqService {

	@Autowired
	BoqRepository boqRepo;

	public BillOfQuantities addBoq(BillOfQuantities boq) {
		boq.setCreationDate(new Date());
		return boqRepo.save(boq);
	}

	public boolean addOneServiceTemplate(BillOfQuantities boq, ServiceTemplate template) {
		return boqRepo.addOneServiceTemplate(boq, template);
	}

	public BillOfQuantities findOne(long id) {
		return boqRepo.findOne(id);
	}

	public boolean boqNameExists(String name) {
		return boqRepo.boqExists(name);
	}

	public void removeAllServiceTemplates(BillOfQuantities boq) {
		boqRepo.removeAllServiceTemplates(boq);
	}

	public List<BillOfQuantities> findAllBoqs() {
		return (ArrayList<BillOfQuantities>) boqRepo.findAll();
	}

	public List<ServiceTemplate> findAllServiceTemplates(BillOfQuantities boq) {
		return boqRepo.findAllServiceTemplates(boq);
	}

	public boolean deleteBoq(long boqId) {
		if (boqRepo.exists(boqId)) {
			boqRepo.delete(boqId);
			return true;
		}
		return false;
	}

	public boolean updateBoq(long boqId, BillOfQuantities boqCopy) {
		BillOfQuantities boq = boqRepo.findOne(boqId);
		removeAllServiceTemplates(boq);
		if (boq != null) {
			if (!boq.getName().equals(boqCopy.getName()))
				boq.setName(boqCopy.getName());
			if (!boq.getStartDate().equals(boqCopy.getStartDate()))
				boq.setStartDate(boqCopy.getStartDate());
			if (!boq.getEndDate().equals(boqCopy.getEndDate()))
				boq.setEndDate(boqCopy.getEndDate());
			boqRepo.save(boq);
			return true;
		}
		return false;
	}

	public boolean updateBoq(long boqId) {
		if (boqRepo.save(boqRepo.findOne(boqId)) != null)
			return true;
		return false;
	}

	public JSONObject jsonBoq(BillOfQuantities boq) {
		JSONObject jsonBoq = new JSONObject().element("id", boq.getId()).element("name", boq.getName())
				.element("startDate", new SimpleDateFormat("dd/MM/yyyy").format(boq.getStartDate()))
				.element("endDate", new SimpleDateFormat("dd/MM/yyyy").format(boq.getEndDate()))
				.element("valid", boq.getEndDate().after(new Date()));
		return jsonBoq;
	}

	public List<BillOfQuantities> findAllAvailableValidBoqs() {
		return boqRepo.findAllAvailableValidBoqs();
	}
}
