package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.omniacom.omniapp.entity.TaskTemplate;

@Component
public class TaskTemplateValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return TaskTemplate.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "task.template.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "estimationTime", "task.template.estimationTime.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "estimationHR", "task.template.estimationHR.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "priority", "task.template.priority.empty");
		
	}

}
