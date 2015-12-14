package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
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
		provider=new Provider("API_KEY", "AUTH_ID", providerName, numberProviderCountries);
		provider.setId(123);
		MockitoAnnotations.initMocks(this);
		when(providerService.getPlivioProvider()).thenReturn(provider);
        when(voiceRouteMasterDao.getResourceById(provider.getId(), country.getCountryCode(),country.getCountryIso())).thenReturn(voiceRouteMaster);
        when(countrySearchService.getCountryByIsoCode(countryIsoCode)).thenReturn(country);
	}
	@Test
	public void provisionNumberWhenProviderPlivo() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		provider.setName(providerName);
		provider.setSms_callback_url("SMS_CALLBACK_URL");
		when(providerService.getPlivioProvider()).thenReturn(provider);
		when(plivoFactoryImpl.updateNumber(number,application, providerService.getPlivioProvider(), userid,
                clientId, groupid, teamid, clientname, clientkey)).thenReturn(applicationResponse);
		ApplicationResponse applicationResponseReturnValue=provisionService.provisionNumber(number,countryIsoCode,provider,userid,clientId,groupid,teamid,clientname,clientkey,application);
		assertNotNull(applicationResponseReturnValue);
		assertEquals("FRIENDLY.NAME", applicationResponseReturnValue.getFriendlyName());
	}
	@Test
	public void provisionNumberWhenProviderTwilio() throws ClientProtocolException, IOException {
		providerName = "TWILIO";
		provider.setName(providerName);
		provider.setSms_callback_url("SMS_CALLBACK_URL");
		when(providerService.getTwilioProvider()).thenReturn(provider);
		when(twilioFactoryImpl.updateNumber(number,application, providerService.getTwilioProvider(), userid,
                clientId, groupid, teamid, clientname, clientkey)).thenReturn(applicationResponse);
		ApplicationResponse applicationResponseReturnValue=provisionService.provisionNumber(number,countryIsoCode,provider,userid,clientId,groupid,teamid,clientname,clientkey,application);
		assertNotNull(applicationResponseReturnValue);
		assertEquals("FRIENDLY.NAME", applicationResponseReturnValue.getFriendlyName());
	}
	
	@Test
	public void provisionNumberWhenProviderNexmo() throws ClientProtocolException, IOException {
		providerName = "NEXMO";
		provider.setName(providerName);
		provider.setSms_callback_url("SMS_CALLBACK_URL");
		when(providerService.getNexmoProvider()).thenReturn(provider);
		when(nexmoFactoryImpl.updateNumber(number,countryIsoCode, application, providerService.getNexmoProvider(), userid,
                clientId, groupid, teamid, clientname, clientkey)).thenReturn(applicationResponse);
		ApplicationResponse applicationResponseReturnValue=provisionService.provisionNumber(number,countryIsoCode,provider,userid,clientId,groupid,teamid,clientname,clientkey,application);
		assertNotNull(applicationResponseReturnValue);
		assertEquals("FRIENDLY.NAME", applicationResponseReturnValue.getFriendlyName());
	}
	
	@Test
	public void provisionNumberWhenProviderInvalid() throws ClientProtocolException, IOException {
		providerName = "";
		provider.setName(providerName);
		provider.setSms_callback_url("SMS_CALLBACK_URL");		
		ApplicationResponse applicationResponseReturnValue = null;
		try{
			applicationResponseReturnValue=provisionService.provisionNumber(number,countryIsoCode,provider,userid,clientId,groupid,teamid,clientname,clientkey,application);
		}
		catch(InboundRestException e){
			assertEquals("Provider " + provider.getName() + " is invalid", e.getDetailedMessage());
			assertEquals(InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION.getCode(), e.getCode());
		}
		assertNull(applicationResponseReturnValue);
	}
	@Test
	public void provisionAllNumbersWhenProviderPlivo() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		provider.setName(providerName);
		when(plivoFactoryImpl.updateAllNumbers(application, providerService.getPlivioProvider(),
				userid, clientId, groupid, teamid, clientname, clientkey)).thenReturn(applicationResponse);
		ApplicationResponse applicationResponseReturnValue=provisionService.provisionAllNumbers(providerName,application,userid,clientId,groupid,teamid,clientname,clientkey);
		assertEquals("FRIENDLY.NAME", applicationResponseReturnValue.getFriendlyName());
	}

	@Test
	public void provisionAllNumbersWhenProviderNotPlivo() throws ClientProtocolException, IOException {
		providerName = "NOT_PLIVO";
		provider.setName(providerName);
		ApplicationResponse applicationResponseReturnValue = null;
		try{
			applicationResponseReturnValue=provisionService.provisionAllNumbers(providerName,application,userid,clientId,groupid,teamid,clientname,clientkey);
		}
		catch(InboundRestException e){
			assertEquals("Provisioning all numbers is not supported by Provider " + providerName, e.getDetailedMessage());
			assertEquals(InboundApiErrorCodes.INVALID_PROVIDER_ACTION_EXCEPTION.getCode(), e.getCode());
		}
		assertNull(applicationResponseReturnValue);
	}

	@Test
	public void updateApplicationWhenProviderPlivo() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		provider.setName(providerName);
		doReturn(applicationResponse).when(plivoFactoryImpl).updateApplication(application, provider);
		ApplicationResponse applicationResponseReturnValue=provisionService.updateApplication(providerName, application);
		assertEquals("FRIENDLY.NAME", applicationResponseReturnValue.getFriendlyName());
	}

	@Test
	public void updateApplicationWhenProviderNotPlivo()throws ClientProtocolException, IOException {
		providerName = "NOT_PLIVO";
		provider.setName(providerName);
		ApplicationResponse applicationResponseReturnValue = null;
		try{
			applicationResponseReturnValue=provisionService.updateApplication(providerName, application);
		}
		catch(InboundRestException e){
			assertEquals("Application cannot be updated for Provider " + providerName, e.getDetailedMessage());
			assertEquals(InboundApiErrorCodes.INVALID_PROVIDER_ACTION_EXCEPTION.getCode(), e.getCode());
		}
		assertNull(applicationResponseReturnValue);
	}

	@Test
	public void createApplicationWhenProviderPlivo() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		provider.setName(providerName);
		doReturn(applicationResponse).when(plivoFactoryImpl).createApplication(application, provider);
		ApplicationResponse applicationResponseReturnValue=provisionService.createApplication(providerName, application);
		assertEquals("FRIENDLY.NAME", applicationResponseReturnValue.getFriendlyName());
	}

	@Test
	public void createApplicationWhenProviderNotPlivo() throws ClientProtocolException, IOException {
		providerName = "NOT_PLIVO";
		provider.setName(providerName);
		ApplicationResponse applicationResponseReturnValue = null;
		try{
			applicationResponseReturnValue=provisionService.createApplication(providerName, application);
		}
		catch(InboundRestException e){
			assertEquals("Application cannot be created for Provider " + providerName, e.getDetailedMessage());
			assertEquals(InboundApiErrorCodes.INVALID_PROVIDER_ACTION_EXCEPTION.getCode(), e.getCode());
		}
		assertNull(applicationResponseReturnValue);
	}

	@Test
	public void getApplicationWhenProviderPlivo() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		app_id = "123";
		provider.setName(providerName);
		doReturn(applicationResponse).when(plivoFactoryImpl).getApplication(app_id, provider);
		ApplicationResponse applicationResponseReturnValue=provisionService.getApplication(providerName, app_id);
		assertEquals("FRIENDLY.NAME", applicationResponseReturnValue.getFriendlyName());
	}

	@Test
	public void getApplicationWhenProviderNotPlivo() throws ClientProtocolException, IOException {
		providerName = "NOT_PLIVO";
		app_id = "123";
		provider.setName(providerName);
		ApplicationResponse applicationResponseReturnValue = null;
		try{
			applicationResponseReturnValue=provisionService.getApplication(providerName, app_id);
		}
		catch(InboundRestException e){
			assertEquals("Application is not available for Provider " + providerName, e.getDetailedMessage());
			assertEquals(InboundApiErrorCodes.INVALID_PROVIDER_ACTION_EXCEPTION.getCode(), e.getCode());
		}
		assertNull(applicationResponseReturnValue);
	}
}
