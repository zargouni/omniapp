package com.omniacom.omniapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Classification;
import com.omniacom.omniapp.repository.ClassificationRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ClassificationService {

	@Autowired
	ClassificationRepository classRepo;
	
	public Classification findById(long id) {
		return classRepo.findOne(id);
	}

	public JSONArray getAllClassifications() {
		JSONArray json = new JSONArray();
		for (Classification c : classRepo.findAll()) {
			json.add(this.jsonClassification(c));
		}
		return json;
	}

	public JSONObject jsonClassification(Classification c) {
		return new JSONObject().element("id", c.getId()).element("name", c.getName());
	}

	public boolean addClassification(Classification c) {
		if(classRepo.save(c) != null)
			return true;
		return false;
	}

}
