package com.omniacom.omniapp.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Nature;
import com.omniacom.omniapp.service.NatureService;

@Component
public class NatureConverter implements Converter<String, Nature> {

	@Autowired
	NatureService natureService;
	
	@Override
	public Nature convert(String name) {
		// TODO Auto-generated method stub
		return natureService.findNature(name);
	}

}
