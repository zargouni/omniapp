package com.omniacom.omniapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Nature;
import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.repository.SiteRepository;

import net.sf.json.JSONObject;

@Service
public class SiteService {
	@Autowired
	SiteRepository siteRepo;

	public Site addSite(Site site) {
		return siteRepo.save(site);
	}

	public void addSites(List<Site> siteList) {
		siteRepo.save(siteList);
	}

	public Site findSite(long id) {
		return siteRepo.findOne(id);
	}

	public boolean deleteSite(long siteId) {
		if (siteRepo.exists(siteId)) {
			Site site = siteRepo.findOne(siteId);
			site.setDeleted(true);
			siteRepo.save(site);
			return true;
		}
		return false;
	}

	public boolean addOneNature(Site site, Nature nature) {
		return siteRepo.addOneNature(site, nature);
	}

	public boolean updateSite(long siteId, Site siteCopy) {
		Site site = siteRepo.findOne(siteId);
		site.getNatures().clear();
		if (site != null) {
			if (!site.getName().equals(siteCopy.getName()))
				site.setName(siteCopy.getName());
			if (!(site.getLatitude() == siteCopy.getLatitude()))
				site.setLatitude(siteCopy.getLatitude());
			if (!(site.getLongitude() == siteCopy.getLongitude()))
				site.setLongitude(siteCopy.getLongitude());
			siteRepo.save(site);
			return true;
		}
		return false;
	}

	public JSONObject jsonSite(Site site) {
		return new JSONObject().element("name", site.getName()).element("latitude", site.getLatitude())
				.element("longitude", site.getLongitude()).element("deleted", site.isDeleted());
	}

}
