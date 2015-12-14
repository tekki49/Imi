package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Providercountry;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;

public class ReleaseNumberServiceTest {
	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	PlivoFactoryImpl plivoFactoryImpl;
	@Mock
	NexmoFactoryImpl nexmoFactoryImpl;
	@InjectMocks
	ReleaseNumberService releaseNumberService;

	Provider provider;
	String number, countryIsoCode, apiKey, authId, name, clientname, clientkey;
	Integer userid, clientId, groupid, teamid;
	Set<Providercountry> numberProviderCountries;

	@Before
	public void setUp() {
		number = "123456789";
		countryIsoCode = "US";
		apiKey = "API_KEY";
		authId = "AUTH_ID";
		name = "NAME";
		clientname = "CLIENT_NAME";
		clientkey = "CLIENT_KEY";
		userid = 103;
		clientId = 102;
		groupid = 101;
		teamid = 100;
		numberProviderCountries = new HashSet<Providercountry>(0);
		provider = new Provider(apiKey, authId, name, numberProviderCountries);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void releaseNumberWhenProviderTwilio() throws ClientProtocolException, IOException {
		provider.setName("TWILIO");
		releaseNumberService.releaseNumber(number, provider, countryIsoCode, userid, clientId, groupid, teamid, clientname, clientkey);
		verify(twilioFactoryImpl, times(1)).releaseNumber(number, provider, countryIsoCode, userid,
				clientId, groupid, teamid, clientname, clientkey);
	}

	@Test
	public void releaseNumberWhenProviderPlivo() throws ClientProtocolException, IOException {
		provider.setName("PLIVO");
		releaseNumberService.releaseNumber(number, provider, countryIsoCode, userid, clientId, groupid, teamid, clientname, clientkey);
		verify(plivoFactoryImpl, times(1)).releaseNumber(number, provider, countryIsoCode, userid,
				clientId, groupid, teamid, clientname, clientkey);
	}

	@Test
	public void releaseNumberWhenProviderNexmo() throws ClientProtocolException, IOException {
		provider.setName("NEXMO");
		releaseNumberService.releaseNumber(number, provider, countryIsoCode, userid, clientId, groupid, teamid, clientname, clientkey);
		verify(nexmoFactoryImpl, times(1)).releaseNumber(number, provider, countryIsoCode, userid,
				clientId, groupid, teamid, clientname, clientkey);
	}
	@Test
	public void releaseNumberWhenProviderInvalid() throws ClientProtocolException, IOException {
		provider.setName("Invalid");
		try{
			releaseNumberService.releaseNumber(number, provider, countryIsoCode, userid, clientId, groupid, teamid, clientname, clientkey);
		}
		catch(InboundRestException e){
			assertEquals("Provider " + provider.getName() + " is invalid", e.getDetailedMessage());
			assertEquals(InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION.getCode(), e.getCode());
		}
	}

}
