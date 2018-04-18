package com.omniacom.omniapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.omniacom.omniapp.entity.BillOfQuantities;

@Component
public class BoqValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return BillOfQuantities.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "boq.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "boq.startDate.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "boq.endDate.empty");
		
		BillOfQuantities boq = (BillOfQuantities) target;
		if (boq.getStartDate() != null && boq.getEndDate() != null) {

			if (boq.getStartDate().after(boq.getEndDate()))
				errors.rejectValue("startDate", "boq.date.nomatch");
			
		}
	}

}
