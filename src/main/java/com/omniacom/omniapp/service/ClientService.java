package com.omniacom.omniapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.repository.ClientRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepo;

	public Iterable<Client> findAllClients() {
		return clientRepo.findAll();
	}

	public Client findById(Long id) {
		return clientRepo.findOne(id);
	}

	public Client findByName(String name) {
		// TODO Auto-generated method stub
		return clientRepo.findByName(name);
	}

	public void addClient(Client client) {
		// TODO Auto-generated method stub
		clientRepo.save(client);

	}

	public boolean clientExists(Client client) {
		Client foundClient = clientRepo.findByName(client.getName());
		if (client.equals(foundClient))
			return true;
		return false;
	}

	public JSONArray findAllSitesJson(Client client) {
		List<Site> sites = clientRepo.findAllSites(client);
		List<JSONObject> jsonArray = new ArrayList<JSONObject>();
		for (Site site : sites) {
			jsonArray.add(jsonSite(site));
		}
		JSONArray json = JSONArray.fromObject(jsonArray);
		return json;
	}

	private JSONObject jsonSite(Site site) {
		JSONObject jsonSite = new JSONObject()
				.element("id", site.getId())
				.element("name", site.getName())
				.element("latitude", site.getLatitude())
				.element("longitude", site.getLongitude());
		return jsonSite;
	}

}
