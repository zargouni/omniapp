package com.omniacom.omniapp.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.service.ServiceService;

@Component
public class ServiceConverter implements Converter<String, Service> {

	@Autowired
	ServiceService serviceService;
	
	@Override
	public Service convert(String id) {
		try {
            Long serviceId = Long.valueOf(id);
            return serviceService.findById(serviceId);
        } catch (NumberFormatException e) {
            return null;
        }
	}

}
