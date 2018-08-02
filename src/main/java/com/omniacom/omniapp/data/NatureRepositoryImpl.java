package com.omniacom.omniapp.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.Nature;
import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.repository.custom.NatureRepositoryCustom;

@Repository
@Transactional
public class NatureRepositoryImpl implements NatureRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<Site> findSites(Nature nature) {
		List<Site> sites = null;
		Query query = entityManager
				.createQuery(
						"SELECT s FROM Site s, Nature n JOIN n.sites sites WHERE n.id = :param AND sites.id = s.id")
				.setParameter("param", nature.getId());
		sites = (List<Site>) query.getResultList();
		return sites;
	}

	@Override
	public boolean exists(String name) {
		Query query = entityManager.createQuery("SELECT n FROM Nature n WHERE n.name=:param").setParameter("param",
				name);
		List<Nature> results = (List<Nature>) query.getResultList();
		if (!results.isEmpty())
			return true;

		return false;
	}

	@Override
	public Nature findOne(String name) {
		Query query = entityManager.createQuery("SELECT n FROM Nature n WHERE n.name=:param").setParameter("param",
				name);
		return (Nature) query.getSingleResult();
	}

	@Override
	public List<Nature> findAllAvailableNatures() {
		List<Nature> natures = null;
		Query query = entityManager.createQuery("SELECT n FROM Nature n WHERE n.deleted = false");
		natures = (List<Nature>) query.getResultList();
		return natures;
	}

}
