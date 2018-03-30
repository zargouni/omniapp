package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.omniacom.omniapp.entity.Task;

@Component
public class TaskValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Task.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "task.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "service", "task.service.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "task.startDate.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "task.endDate.empty");

		Task task = (Task) target;
		if(task.getStartDate() != null && task.getEndDate() != null)
		if (task.getStartDate().after(task.getEndDate()))
			errors.rejectValue("startDate", "task.date.nomatch");

	}

}
