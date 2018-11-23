//package com.omniacom.omniapp.validator;
//
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.ValidationUtils;
//import org.springframework.validation.Validator;
//
//import com.omniacom.omniapp.entity.Snag;
//
//@Component
//public class SnagValidator implements Validator{
//
//	@Override
//	public boolean supports(Class<?> clazz) {
//		// TODO Auto-generated method stub
//		return Snag.class.equals(clazz);
//	}
//
//	@Override
//	public void validate(Object target, Errors errors) {
//		// TODO Auto-generated method stub
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "snag.title.empty");
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "snag.content.empty");
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "severity", "snag.severity.empty");
//		
//	}
//
//}
