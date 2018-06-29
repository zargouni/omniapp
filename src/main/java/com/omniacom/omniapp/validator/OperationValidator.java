package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.omniacom.omniapp.entity.Operation;

@Component
public class OperationValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Operation.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "project", "operation.project.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "operation.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "operation.startDate.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "operation.endDate.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "site", "operation.site.empty");

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "responsible", "operation.responsible.empty");

		Operation operation = (Operation) target;
		if (operation.getStartDate() != null && operation.getEndDate() != null) {

			if (operation.getStartDate().after(operation.getEndDate()))
				errors.reject("operation.date.nomatch");
			
			
		}
		
		
	}

}
