package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;

@Service
public class ReleaseNumberService implements ProviderConstants {

	@Autowired
	TwilioFactoryImpl twilioFactoryImpl;
	@Autowired
	PlivoFactoryImpl plivioFactoryImpl;
	@Autowired
	NexmoFactoryImpl nexmoFactoryImpl;

	public void releaseNumber(String number, Provider provider, String countryIsoCode, Integer userid, Integer clientId,
			Integer groupid, Integer teamid, String clientname, String clientkey)
					throws ClientProtocolException, IOException {
		if (provider.getName().equalsIgnoreCase(TWILIO)) {
			twilioFactoryImpl.releaseNumber(number, provider, countryIsoCode, userid, clientId, groupid, teamid,
					clientname, clientkey);
		} else if (provider.getName().equalsIgnoreCase(PLIVO)) {
			plivioFactoryImpl.releaseNumber(number, provider, countryIsoCode, userid, clientId, groupid, teamid,
					clientname, clientkey);
		} else if (provider.getName().equalsIgnoreCase(NEXMO)) {
			nexmoFactoryImpl.releaseNumber(number, provider, countryIsoCode, userid, clientId, groupid, teamid,
					clientname, clientkey);
		} else {
			String message = "Provider " + provider.getName() + " is invalid";
			throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION, message);
		}
	}

}
