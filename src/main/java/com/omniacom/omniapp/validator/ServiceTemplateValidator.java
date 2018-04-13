package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.omniacom.omniapp.entity.ServiceTemplate;

@Component
public class ServiceTemplateValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return ServiceTemplate.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "service.template.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "service.template.description.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "service.template.price.empty");
		
		ServiceTemplate template = (ServiceTemplate) target;
		if(template.getPrice() <= 0)
			errors.rejectValue("price", "service.template.price.undefined");


		
	}

}
