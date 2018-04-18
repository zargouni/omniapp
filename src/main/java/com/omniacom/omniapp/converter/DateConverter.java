package com.omniacom.omniapp.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class DateConverter implements Converter<String , Date> {
	
	@Override
	public Date convert(String source) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return sdf.parse(source);
		} catch (ParseException e) {
			return null;
		}
	}

}
