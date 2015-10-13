package com.imi.rest.util;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

public class BasicAuthUtil {

	public static String getBasicAuthHash(String provider) {
		String authId = null;
		String authToken = null;
		if (provider.equals("PLIVIO")) {
			authId = "MANMMWNGMWMZNKNDIWOD";
			authToken = "YmM4MWU3MTQxZTk1OTZkMGM2ZmIxYWM1YTBmNWY0";
		}
		if (provider.equals("TWILIO")) {
			authId = "AC606f86ee4172ff7773d4162e7b62496c";
			authToken = "9e2928235fceefb8c92c39a4ceabc0b8";
		}
		String unhashedString = authId + ":" + authToken;
		byte[] authBytes = unhashedString.getBytes(StandardCharsets.UTF_8);
		return DatatypeConverter.printBase64Binary(authBytes);
	}
	
}
