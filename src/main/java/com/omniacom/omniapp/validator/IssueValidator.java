package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.omniacom.omniapp.entity.Issue;

@Component
public class IssueValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Issue.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "issue.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "issue.description.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "issue.endDate.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "severity", "issue.severity.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "operation", "issue.operation.empty");
			
	}
	
}
