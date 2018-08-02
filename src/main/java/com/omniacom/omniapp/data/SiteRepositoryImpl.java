package com.omniacom.omniapp.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omniacom.omniapp.entity.Nature;
import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.repository.custom.SiteRepositoryCustom;

@Repository
@Transactional
public class SiteRepositoryImpl implements SiteRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public boolean addOneNature(Site site, Nature nature) {
		// if (!natureExists(site, nature)) {
		// if (site.getNatures() != null) {
		return site.getNatures().add(nature);
		// }
		// site.setNatures(new ArrayList<>());
		// return site.getNatures().add(nature);
		// }
		// return false;
	}

}
