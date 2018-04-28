package com.omniacom.omniapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.repository.SiteRepository;

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
	
	

}
