package com.omniacom.omniapp.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Nature;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.repository.custom.ClientRepositoryCustom;

@Repository
@Transactional
public class ClientRepositoryImpl implements ClientRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Site> findAllSites(Client client) {
		List<Site> sites = null;
		Query query = entityManager.createQuery("SELECT s FROM Site s WHERE s.client.id = :param AND s.deleted = false")
				.setParameter("param", client.getId());
		sites = (List<Site>) query.getResultList();
		return sites;
	}
	
	@Override
	public List<Project> findAllProjects(Client client) {
		List<Project> projects = null;
		Query query = entityManager.createQuery("SELECT p FROM Project p WHERE p.client.id = :param")
				.setParameter("param", client.getId());
		projects = (List<Project>) query.getResultList();
		return projects;	}

	@Override
	public Client findByName(String name) {
		Client client = null;
		Query query = entityManager
				.createQuery("SELECT c FROM Client c WHERE c.name=:param")
				.setParameter("param",name);
		List<Client> results = (List<Client>) query.getResultList();
		if (!results.isEmpty())
			// ignores multiple results
			client = (Client) results.get(0);

		return client;
	}

	@Override
	public boolean siteExists(Client client,Site site) {
		Query query = entityManager.createQuery(
				"SELECT s FROM Site s WHERE s.name = :param AND client.id = :clientId")
				.setParameter("param", site.getName()).setParameter("clientId", client.getId());
		if (query.getResultList().size() != 0)
			return true;
		return false;
	}

	@Override
	public List<Site> findAllSitesByNature(Nature nature, Client client) {
		List<Site> sites = null;
		Query query = entityManager.createQuery("SELECT s FROM Site s JOIN s.natures nature WHERE s.client.id = :param AND nature.id = :natureId AND s.deleted = false")
				.setParameter("param", client.getId())
				.setParameter("natureId", nature.getId());
		sites = (List<Site>) query.getResultList();
		return sites;
	}

}
