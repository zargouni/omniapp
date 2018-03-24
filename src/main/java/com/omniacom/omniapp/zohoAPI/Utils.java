package com.omniacom.omniapp.zohoAPI;

public class Utils {

	public static String copyAuthTokenOnly(String response) {
		int start = response.indexOf("=") + 1;

		return response.substring(start, response.indexOf("RESULT") - 1).trim();
	}

}
