package com.imi.rest.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
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
import com.imi.rest.dao.VoiceRouteMasterDao;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Providercountry;
import com.imi.rest.dao.model.VoiceRouteMaster;
import com.imi.rest.model.ApplicationResponse;

public class ProvisionServiceTest {

	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	PlivoFactoryImpl plivoFactoryImpl;
	@Mock
	NexmoFactoryImpl nexmoFactoryImpl;
	@Mock
	ProviderService providerService;
	@Mock
    VoiceRouteMasterDao voiceRouteMasterDao;
	@Mock
	CountrySearchService countrySearchService;
	@InjectMocks
	ProvisionService provisionService;
	
	Provider provider;
	String number;
	String countryIsoCode;
	String providerName;
	String app_id;
	Integer userid;
	Integer clientId;
	Integer groupid;
	Integer teamid;
	String clientname;
	String clientkey;
	Country country;
	ApplicationResponse application;
	ApplicationResponse applicationResponse;
	VoiceRouteMaster voiceRouteMaster;
	Set<Providercountry> numberProviderCountries;

	@Before
	public void setUp(){
		applicationResponse = new ApplicationResponse();
		application=new ApplicationResponse();
		voiceRouteMaster=new VoiceRouteMaster();
		voiceRouteMaster.setCountryCode("COUNTRY_CODE");
		voiceRouteMaster.setDvpCallbackUrl("DVP_CALLBACK_URL");
		applicationResponse.setFriendlyName("FRIENDLY.NAME");
		application.setFriendly_name("FRIENDLY_NAME_2");
		country=new Country();
		countryIsoCode="US";
		country.setCountry("UNITED_STATES");
		country.setCountryCode("COUNTRY_CODE");
		country.setCountryIso(countryIsoCode);
		userid=7123;
		clientId=700;
		groupid=900;
		teamid=912;
		number="123123123";
		clientname="CLIENT_NAME";
		clientkey="CLIENT_KEY";
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void provisionNumberWhenProviderTwilio() throws ClientProtocolException, IOException {
		providerName = "TWILIO";
		provider=new Provider("API_KEY", "AUTH_ID", providerName, numberProviderCountries);
		provider.setId(123);
		provider.setSms_callback_url("SMS_CALLBACK_URL");
		when(providerService.getTwilioProvider()).thenReturn(provider);
        when(voiceRouteMasterDao.getResourceById(provider.getId(), country.getCountryCode(),country.getCountryIso())).thenReturn(voiceRouteMaster);
        when(countrySearchService.getCountryByIsoCode(countryIsoCode)).thenReturn(country);
		when(twilioFactoryImpl.updateNumber(number,application, providerService.getTwilioProvider(), userid,
                clientId, groupid, teamid, clientname, clientkey)).thenReturn(applicationResponse);
		ApplicationResponse applicationResponseReturnValue=provisionService.provisionNumber(number,countryIsoCode,provider,userid,clientId,groupid,teamid,clientname,clientkey);
		assertNotNull(applicationResponseReturnValue);
		assertEquals("FRIENDLY.NAME", applicationResponseReturnValue.getFriendlyName());
	}

	/*@Test
	public void provisionNumberWhenProviderPlivo() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		doReturn(applicationResponse).when(plivoFactoryImpl).updateNumber(number, application, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void provisionNumberWhenProviderNexmo() throws ClientProtocolException, IOException {
		providerName = "NEXMO";
		doReturn(applicationResponse).when(nexmoFactoryImpl).updateNumber(number, countryIsoCode, application,
				provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void provisionNumberWhenProviderNeither() throws ClientProtocolException, IOException {
		providerName = "NEITHER";
		doThrow(new InvalidProviderException(5)).when(nexmoFactoryImpl).updateNumber(number, countryIsoCode,
				application, provider);
	}

	@Test
	public void provisionAllNumbersWhenProviderPlivo() ClientProtocolException, IOException {
		providerName = "PLIVO";
		doReturn(applicationResponse).when(plivoFactoryImpl).updateAllNumbers(application, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void provisionAllNumbersWhenProviderNotPlivo() throws ClientProtocolException, IOException {
		providerName = "NOT_PLIVO";
		doThrow(new InvalidProviderException(5)).when(plivoFactoryImpl).updateNumber(number, application, provider);
	}

	@Test
	public void updateApplicationWhenProviderPlivo() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		doReturn(applicationResponse).when(plivoFactoryImpl).updateApplication(application, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void updateApplicationWhenProviderNotPlivo()throws ClientProtocolException, IOException {
		providerName = "NOT_PLIVO";
		doThrow(new InvalidProviderException(5)).when(plivoFactoryImpl).updateApplication(application, provider);
	}

	@Test
	public void createApplicationWhenProviderPlivo() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		doReturn(applicationResponse).when(plivoFactoryImpl).createApplication(application, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void createApplicationWhenProviderNotPlivo() throws ClientProtocolException, IOException {
		providerName = "NOT_PLIVO";
		doThrow(new InvalidProviderException(5)).when(plivoFactoryImpl).createApplication(application, provider);
	}

	@Test
	public void getApplicationWhenProviderPlivo() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		doReturn(applicationResponse).when(plivoFactoryImpl).getApplication(app_id, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void getApplicationWhenProviderNotPlivo() throws ClientProtocolException, IOException {
		providerName = "NOT_PLIVO";
		doThrow(new InvalidProviderException(5)).when(plivoFactoryImpl).getApplication(app_id, provider);
	}*/
}
