package com.imi.rest.service;

import static org.mockito.Mockito.doThrow;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Providercountry;

public class ReleaseNumberServiceTest {
	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	PlivoFactoryImpl plivioFactoryImpl;
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
		twilioFactoryImpl = Mockito.mock(TwilioFactoryImpl.class);
		doThrow(new RuntimeException()).when(twilioFactoryImpl).releaseNumber(number, provider, countryIsoCode, userid,
				clientId, groupid, teamid, clientname, clientkey);
	}

	@Test
	public void releaseNumberWhenProviderPlivo() throws ClientProtocolException, IOException {
		plivioFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		doThrow(new RuntimeException()).when(plivioFactoryImpl).releaseNumber(number, provider, countryIsoCode, userid,
				clientId, groupid, teamid, clientname, clientkey);
	}

	@Test
	public void releaseNumberWhenProviderNexmo() throws ClientProtocolException, IOException {
		nexmoFactoryImpl = Mockito.mock(NexmoFactoryImpl.class);
		doThrow(new RuntimeException()).when(nexmoFactoryImpl).releaseNumber(number, provider, countryIsoCode, userid,
				clientId, groupid, teamid, clientname, clientkey);
	}

}
