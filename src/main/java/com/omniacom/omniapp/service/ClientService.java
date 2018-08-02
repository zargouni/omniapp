package com.omniacom.omniapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Nature;
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
//		if (client.equals(foundClient))
//			return true;
		if(foundClient != null)
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

	public Integer findSitesCount(Client client) {
		return clientRepo.findAllSites(client).size();
	}

	public JSONArray findAllClientsJson() {
		JSONArray json = new JSONArray();
		List<Client> clients = (List<Client>) findAllClients();
		for (Client client : clients) {
			json.add(jsonClient(client));
		}
		return json;
	}

	public boolean siteExists(Client client, Site site) {
		return clientRepo.siteExists(client, site);
	}

	private JSONObject jsonSite(Site site) {
		String str = "";
		List<Nature> natures = site.getNatures();
		if(!natures.isEmpty())
		for(Nature n : natures) {
			str += n.getName()+" ";
		}
		JSONObject jsonSite = new JSONObject().element("id", site.getId()).element("name", site.getName())
				.element("latitude", site.getLatitude())
				.element("longitude", site.getLongitude())
				.element("natures",str);
		return jsonSite;
	}

	public JSONObject jsonClient(Client client) {
		JSONObject jsonClient = new JSONObject().element("id", client.getId()).element("name", client.getName())
				.element("email", client.getEmail()).element("address", client.getAddress())
				.element("country", client.getCountry()).element("phone", client.getPhone())
				.element("sitesCount", findSitesCount(client))
				.element("logo", client.getLogo() == null ? "assets/app/media/img/logos/no_logo.jpg" : client.getLogo());
		return jsonClient;
	}

	public Integer projectsCount(Client client) {
		return clientRepo.findAllProjects(client).size();
	}

	public Integer moneyCount(Client client) {
		return clientRepo.findAllProjects(client).size();
	}

	public JSONArray findAllSitesByNatureJson(Nature nature, Client client) {
		List<Site> sites = clientRepo.findAllSitesByNature(nature,client);
		List<JSONObject> jsonArray = new ArrayList<JSONObject>();
		for (Site site : sites) {
			jsonArray.add(jsonSite(site));
		}
		JSONArray json = JSONArray.fromObject(jsonArray);
		return json;
	}
	
	public boolean updateClient(long clientId, Client clientCopy) {
		Client client = clientRepo.findOne(clientId);
		if (client != null) {
			if (!client.getName().equals(clientCopy.getName()))
				client.setName(clientCopy.getName());
			if (!client.getEmail().equals(clientCopy.getEmail()))
				client.setEmail(clientCopy.getEmail());
			if (!client.getAddress().equals(clientCopy.getAddress()))
				client.setAddress(clientCopy.getAddress());
			if (!client.getCountry().equals(clientCopy.getCountry()))
				client.setCountry(clientCopy.getCountry());
			if (!client.getPhone().equals(clientCopy.getPhone()))
				client.setPhone(clientCopy.getPhone());
			clientRepo.save(client);
			return true;
		}
		return false;
	}

}
