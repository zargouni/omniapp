package com.omniacom.omniapp.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.service.ClientService;

@Component
public class ClientConverter implements Converter<String, Client>{

	@Autowired
	private ClientService clientService;
	
	@Override
	public Client convert(String id) {
		try {
            Long clientId = Long.valueOf(id);
            return clientService.findById(clientId);
        } catch (NumberFormatException e) {
            return null;
        }
	}

}
