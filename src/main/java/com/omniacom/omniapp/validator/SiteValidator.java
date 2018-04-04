package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SiteValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "site.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "longitude", "site.longitude.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "latitude", "site.latitude.empty");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "client", "site.client.empty");
		
	}

}
