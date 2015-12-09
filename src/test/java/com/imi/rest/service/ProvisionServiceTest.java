package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.exception.InvalidProviderException;
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
	Provider provider;
	@Mock
	String number;
	@Mock
	String countryIsoCode;
	@Mock
	String providerName;
	@Mock
	String app_id;
	@Mock
	ApplicationResponse application;

	@Test
	public void provisionNumberWhenProviderTwilio() throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		twilioFactoryImpl = Mockito.mock(TwilioFactoryImpl.class);
		applicationResponse.setFriendlyName("FRIENDLY.NAME");
		providerName = "TWILO";
		doReturn(applicationResponse).when(twilioFactoryImpl).updateNumber(number, application, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void provisionNumberWhenProviderPlivo() throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		applicationResponse.setFriendlyName("FRIENDLY.NAME");
		providerName = "PLIVO";
		doReturn(applicationResponse).when(plivoFactoryImpl).updateNumber(number, application, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void provisionNumberWhenProviderNexmo() throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		nexmoFactoryImpl = Mockito.mock(NexmoFactoryImpl.class);
		applicationResponse.setFriendlyName("FRIENDLY.NAME");
		providerName = "NEXMO";
		doReturn(applicationResponse).when(nexmoFactoryImpl).updateNumber(number, countryIsoCode, application,
				provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void provisionNumberWhenProviderNeither() throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		nexmoFactoryImpl = Mockito.mock(NexmoFactoryImpl.class);
		applicationResponse.setFriendlyName("FRIENDLY.NAME");
		providerName = "NEITHER";
		doThrow(new InvalidProviderException(5)).when(nexmoFactoryImpl).updateNumber(number, countryIsoCode,
				application, provider);
	}

	@Test
	public void provisionAllNumbersWhenProviderPlivo() ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		providerName = "PLIVO";
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		applicationResponse.setFriendly_name("FRIENDLY.NAME");
		doReturn(applicationResponse).when(plivoFactoryImpl).updateAllNumbers(application, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void provisionAllNumbersWhenProviderNotPlivo() throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		providerName = "NOT_PLIVO";
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		applicationResponse.setFriendly_name("FRIENDLY.NAME");
		doThrow(new InvalidProviderException(5)).when(plivoFactoryImpl).updateNumber(number, application, provider);
	}

	@Test
	public void updateApplicationWhenProviderPlivo() throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		providerName = "PLIVO";
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		applicationResponse.setFriendly_name("FRIENDLY.NAME");
		doReturn(applicationResponse).when(plivoFactoryImpl).updateApplication(application, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void updateApplicationWhenProviderNotPlivo()throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		providerName = "NOT_PLIVO";
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		applicationResponse.setFriendly_name("FRIENDLY.NAME");
		doThrow(new InvalidProviderException(5)).when(plivoFactoryImpl).updateApplication(application, provider);
	}

	@Test
	public void createApplicationWhenProviderPlivo() throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		providerName = "PLIVO";
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		applicationResponse.setFriendly_name("FRIENDLY.NAME");
		doReturn(applicationResponse).when(plivoFactoryImpl).createApplication(application, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void createApplicationWhenProviderNotPlivo() throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		providerName = "NOT_PLIVO";
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		applicationResponse.setFriendly_name("FRIENDLY.NAME");
		doThrow(new InvalidProviderException(5)).when(plivoFactoryImpl).createApplication(application, provider);
	}

	@Test
	public void getApplicationWhenProviderPlivo() throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		providerName = "PLIVO";
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		applicationResponse.setFriendly_name("FRIENDLY.NAME");
		doReturn(applicationResponse).when(plivoFactoryImpl).getApplication(app_id, provider);
		assertEquals("FRIENDLY.NAME", applicationResponse.getFriendlyName());
	}

	@Test
	public void getApplicationWhenProviderNotPlivo() throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		providerName = "NOT_PLIVO";
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		applicationResponse.setFriendly_name("FRIENDLY.NAME");
		doThrow(new InvalidProviderException(5)).when(plivoFactoryImpl).getApplication(app_id, provider);
	}
}
