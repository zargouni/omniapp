package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.omniacom.omniapp.entity.Project;


@Component
public class ProjectValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "project.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "client", "project.client.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "country", "project.country.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currency", "project.currency.empty");
		//ValidationUtils.rejectIfEmpty(errors, "owner", "project.owner.empty");

		


	}

}
