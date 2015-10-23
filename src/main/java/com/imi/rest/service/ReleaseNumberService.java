package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.core.ReleaseNumber;
import com.imi.rest.core.impl.NexmoSearchImpl;
import com.imi.rest.core.impl.PlivioSearchImpl;
import com.imi.rest.core.impl.TwilioSearchImpl;

public class ReleaseNumberService implements ReleaseNumber, ProviderConstants{

	@Autowired
	TwilioSearchImpl twilioSearchImpl;
	@Autowired
	PlivioSearchImpl plivioSearchImpl; 
	@Autowired
	NexmoSearchImpl nexmoSearchImpl;
	
	@Override
	public void releaseNumber(String number, String provider) {
		
	}

	@Override
	public void releaseNumber(String number, String provider,
			String countryIsoCode) throws ClientProtocolException, IOException {
		switch (provider) {
		case TWILIO:
			twilioSearchImpl.releaseNumber(number, provider, countryIsoCode);
			break;
		case PLIVIO:
			plivioSearchImpl.releaseNumber(number, provider, countryIsoCode);
			break;
		case NEXMO:
			nexmoSearchImpl.releaseNumber(number, provider, countryIsoCode);
			break;
		default:
			break;
		}
	}

}
