package com.imi.rest.service;

import com.imi.rest.constants.ProviderConstants;

public class ProviderService implements ProviderConstants {

	public String getProviderById(String providerId){
		String provider = "";
			switch (providerId.trim()) {
			case "1":
				provider = TWILIO;
				break;
			case "2":
				provider = PLIVIO;
				break;
			case "3":
				provider = NEXMO;
				break;
			default:
				break;
		}
		
		return provider;
	}
}
