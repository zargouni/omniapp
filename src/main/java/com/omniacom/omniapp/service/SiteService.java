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
	
	public void addSite(Site site) {
		siteRepo.save(site);
	}
	
	public void addSites(List<Site> siteList) {
		siteRepo.save(siteList);
	}

}
