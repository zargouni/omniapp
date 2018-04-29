package com.omniacom.omniapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.repository.custom.SiteRepositoryCustom;

public interface SiteRepository extends CrudRepository<Site, Long>, SiteRepositoryCustom{

}
