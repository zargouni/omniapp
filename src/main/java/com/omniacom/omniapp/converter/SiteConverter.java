package com.omniacom.omniapp.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.omniacom.omniapp.entity.Site;
import com.omniacom.omniapp.repository.SiteRepository;

public class SiteConverter implements Converter<Long, Site> {

	@Autowired
	SiteRepository siteRepo;

	@Override
	public Site convert(Long siteId) {
		
			return siteRepo.findOne(siteId);
		
	}

}
