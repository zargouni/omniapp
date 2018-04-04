package com.omniacom.omniapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.repository.ClientRepository;

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

}
