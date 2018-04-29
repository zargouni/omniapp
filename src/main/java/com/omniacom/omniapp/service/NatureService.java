package com.omniacom.omniapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Nature;
import com.omniacom.omniapp.repository.NatureRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class NatureService {

	@Autowired
	NatureRepository natureRepo;
	
	public Nature findNature(String name) {
		return natureRepo.findOne(name);
	}
	public boolean natureExists(Nature nature) {
		return natureRepo.exists(nature.getName());
	}
	
	public Nature addNature(Nature nature) {
		return natureRepo.save(nature);
	}
	
	public boolean deleteNature(long natureId) {
		if (natureRepo.exists(natureId)) {
			Nature nature = natureRepo.findOne(natureId);
			nature.setDeleted(true);
			natureRepo.save(nature);
			return true;
		}
		return false;
	}
	
	public JSONArray findAllNaturesJson() {
		List<Nature> natures = (List<Nature>) natureRepo.findAllAvailableNatures();
		JSONArray json = new JSONArray();
		for(Nature nature : natures) {
			json.add(jsonNature(nature));
		}
		return json;
	}
	
	public List<Nature> findAllNatures() {
		return (List<Nature>) natureRepo.findAllAvailableNatures();
		
		
	}
	
	public JSONObject jsonNature(Nature nature) {
		return new JSONObject()
				.element("id", nature.getId())
				.element("name", nature.getName())
				.element("description", nature.getDescription());
	}
}
