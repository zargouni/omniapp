package com.omniacom.omniapp.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Classification;
import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.service.ClassificationService;

@Component
public class ClassificationConverter implements Converter<String, Classification> {

	@Autowired
	ClassificationService classService;
	
	@Override
	public Classification convert(String source) {
		try {
            Long classId = Long.valueOf(source);
            return classService.findById(classId);
        } catch (NumberFormatException e) {
            return null;
        }
	}

}
