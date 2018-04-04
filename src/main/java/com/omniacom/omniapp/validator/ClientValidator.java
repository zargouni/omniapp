package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.omniacom.omniapp.entity.Client;


@Component
public class ClientValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Client.class.equals(clazz);
		
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "client.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "client.email.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "client.phone.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "country", "client.country.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "client.address.empty");
	}

}
