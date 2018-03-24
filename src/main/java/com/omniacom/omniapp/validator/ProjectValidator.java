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
		
		ValidationUtils.rejectIfEmpty(errors, "name", "Project name must not be empty");
		ValidationUtils.rejectIfEmpty(errors, "client", "Select project's client");
		ValidationUtils.rejectIfEmpty(errors, "country", "Select project's country");
		ValidationUtils.rejectIfEmpty(errors, "currency", "Select project's currency");
		//ValidationUtils.rejectIfEmpty(errors, "owner", "project.owner.empty");

		


	}

}
