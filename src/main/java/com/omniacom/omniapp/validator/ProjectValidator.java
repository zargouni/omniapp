package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class ProjectValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		ValidationUtils.rejectIfEmpty(errors, "name", "project.name.empty");
		ValidationUtils.rejectIfEmpty(errors, "client", "project.client.empty");
		ValidationUtils.rejectIfEmpty(errors, "country", "project.country.empty");
		ValidationUtils.rejectIfEmpty(errors, "currency", "project.currency.empty");
		//ValidationUtils.rejectIfEmpty(errors, "owner", "project.owner.empty");

		


	}

}
