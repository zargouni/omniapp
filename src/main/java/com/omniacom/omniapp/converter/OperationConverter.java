package com.omniacom.omniapp.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.service.OperationService;

@Component
public class OperationConverter implements Converter<String, Operation> {

	@Autowired
	OperationService operationService;
	
	@Override
	public Operation convert(String id) {
		try {
            Long opId = Long.valueOf(id);
            return operationService.findOne(opId);
        } catch (NumberFormatException e) {
            return null;
        }
	}

}
