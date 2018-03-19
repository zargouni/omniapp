package com.omniacom.omniapp.repository.custom;

import java.util.List;

import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.entity.Site;

public interface ClientRepositoryCustom {
	
	public List<Site> findAllSites(Client client);
	
	public List<Project> findAllProjects(Client client);

}
