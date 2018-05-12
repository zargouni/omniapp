package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.omniacom.omniapp.entity.Service;

@Component
public class ServiceValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Service.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "service.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "project", "service.project.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "operation", "service.operation.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "priceHT", "service.price.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "category", "service.category.empty");

		
		Service service = (Service) target;
		if(service.getPriceHT() <= 0)
			errors.rejectValue("priceHT", "service.price.undefined");


		
	}

}