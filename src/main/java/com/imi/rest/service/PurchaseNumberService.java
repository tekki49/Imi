package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.core.impl.NexmoSearchImpl;
import com.imi.rest.core.impl.PlivioSearchImpl;
import com.imi.rest.core.impl.TwilioSearchImpl;

public class PurchaseNumberService implements ProviderConstants, UrlConstants {

	@Autowired
	PlivioSearchImpl plivioSearchImpl;
	
	@Autowired
	NexmoSearchImpl nexmoSearchImpl;
	
	@Autowired
	TwilioSearchImpl twilioSearchImpl;
	
	public void purchaseNumber(String number, String provider, String countryIsoCode) throws ClientProtocolException, IOException{
		switch (provider) {
		case TWILIO:
			twilioSearchImpl.purchaseNumber(number, provider, countryIsoCode);
			break;
		case PLIVIO:
			plivioSearchImpl.purchaseNumber(number, provider, countryIsoCode);
			break;
		case NEXMO:
			nexmoSearchImpl.purchaseNumber(number, provider, countryIsoCode);
			break;
		default:
			break;
		}
	}
}
