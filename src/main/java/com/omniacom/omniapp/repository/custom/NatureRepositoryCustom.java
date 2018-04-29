package com.omniacom.omniapp.repository.custom;

import java.util.List;

import com.omniacom.omniapp.entity.Nature;
import com.omniacom.omniapp.entity.Site;

public interface NatureRepositoryCustom {
	
	public List<Site> findSites(Nature nature);
	
	public boolean exists(String name);
	
	public Nature findOne(String name);
	
	public List<Nature> findAllAvailableNatures();
}
