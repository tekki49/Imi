package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;


import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.Meta;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;


public class NumberSearchServiceTest {

	@Mock
	PlivoFactoryImpl plivoFactoryImpl;
	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	NexmoFactoryImpl nexmoFactoryImpl;
	@Mock
	ProviderService providerService;
	@InjectMocks
	NumberSearchService numberSearchService;
	
	@Value(value = "${search.skip.twilio}")
	boolean skipTwilio;

	@Value(value = "${search.skip.nexmo}")
	boolean skipNexmo;

	@Value(value = "${search.skip.plivo}")
	boolean skipPlivo;
	
	ServiceConstants serviceTypeEnum;
	NumberResponse numberResponse;
	Provider provider;
	String countryIsoCode, numberType, pattern, nextPlivoIndex, nextNexmoIndex, twilioIndex, markup, providerName;
	
	@Before
	public void setUp(){
		serviceTypeEnum = ServiceConstants.SMS;
		countryIsoCode = "US";
		numberType = "MOBILE";
		nextPlivoIndex = "2";
		nextNexmoIndex = "3";
		pattern = "123";
		markup = "GBP";
		provider = new Provider();
		provider.setId(1);
		numberResponse = new NumberResponse();
		numberResponse.setCount(1);
		providerName = "";
		provider.setName(providerName);
		MockitoAnnotations.initMocks(this);
		when(providerService.getPlivioProvider()).thenReturn(provider);
	}

	@Test
	public void searchPhoneNumbersProviderPlivo() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		provider.setName("PLIVO");
		numberSearchService.searchPhoneNumbers(serviceTypeEnum,provider, countryIsoCode, numberType, pattern,
				nextPlivoIndex, nextNexmoIndex, markup);
		verify(plivoFactoryImpl, times(1)).searchPhoneNumbers(Mockito.any(Provider.class), Mockito.any(ServiceConstants.class),
				Mockito.any(String.class), Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class)
				, Mockito.any(NumberResponse.class), Mockito.any(String.class));
	}
	
	@Test
	public void searchPhoneNumbersProviderTwilio() throws ClientProtocolException, IOException {
		providerName = "TWILIO";
		provider.setName("TWILIO");
		numberSearchService.searchPhoneNumbers(serviceTypeEnum,provider, countryIsoCode, numberType, pattern,
				nextPlivoIndex, nextNexmoIndex, markup);
		verify(twilioFactoryImpl, times(1)).searchPhoneNumbers(Mockito.any(Provider.class), Mockito.any(ServiceConstants.class),
				Mockito.any(String.class), Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class)
				, Mockito.any(NumberResponse.class), Mockito.any(String.class));
	}
	
	@Test
	public void searchPhoneNumbersProviderNexmo() throws ClientProtocolException, IOException {
		providerName = "NEXMO";
		provider.setName("NEXMO");
		numberSearchService.searchPhoneNumbers(serviceTypeEnum,provider, countryIsoCode, numberType, pattern,
				nextPlivoIndex, nextNexmoIndex, markup);
		verify(nexmoFactoryImpl, times(1)).searchPhoneNumbers(Mockito.any(Provider.class), Mockito.any(ServiceConstants.class),
				Mockito.any(String.class), Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class)
				, Mockito.any(NumberResponse.class), Mockito.any(String.class));
	}
	
	@Test
	public void searchPhoneNumbersProviderInvalid() throws ClientProtocolException, IOException {
		try{
			numberSearchService.searchPhoneNumbers(serviceTypeEnum,provider, countryIsoCode, numberType, pattern,
					nextPlivoIndex, nextNexmoIndex, markup);
		}
		catch(InboundRestException e){
			assertEquals("Provider " + provider.getName() + " is invalid", e.getDetailedMessage());
			assertEquals(InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION.getCode(), e.getCode());
		}
	}
	
	@Test
	public void searchPhoneNumbersPlivoProviderNotPassed() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		provider.setName("PLIVO");
		if(!skipPlivo){
			numberSearchService.searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType, pattern,
					nextPlivoIndex, nextNexmoIndex, markup);
			verify(plivoFactoryImpl, times(1)).searchPhoneNumbers(Mockito.any(Provider.class), Mockito.any(ServiceConstants.class),
					Mockito.any(String.class), Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class)
					, Mockito.any(NumberResponse.class), Mockito.any(String.class));
		}
		else{
			numberSearchService.searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType, pattern,
					nextPlivoIndex, nextNexmoIndex, markup);
			verify(plivoFactoryImpl, times(0)).searchPhoneNumbers(Mockito.any(Provider.class), Mockito.any(ServiceConstants.class),
					Mockito.any(String.class), Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class)
					, Mockito.any(NumberResponse.class), Mockito.any(String.class));
		}		
	}
	
	@Test
	public void searchPhoneNumbersTwilioProviderNotPassed() throws ClientProtocolException, IOException {
		providerName = "TWILIO";
		provider.setName("TWILIO");
		if(!skipTwilio){
			numberSearchService.searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType, pattern,
					nextPlivoIndex, nextNexmoIndex, markup);
			verify(twilioFactoryImpl, times(1)).searchPhoneNumbers(Mockito.any(Provider.class), Mockito.any(ServiceConstants.class),
					Mockito.any(String.class), Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class)
					, Mockito.any(NumberResponse.class), Mockito.any(String.class));
		}
		else{
			numberSearchService.searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType, pattern,
					nextPlivoIndex, nextNexmoIndex, markup);
			verify(twilioFactoryImpl, times(0)).searchPhoneNumbers(Mockito.any(Provider.class), Mockito.any(ServiceConstants.class),
					Mockito.any(String.class), Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class)
					, Mockito.any(NumberResponse.class), Mockito.any(String.class));
		}		
	}
	
	@Test
	public void searchPhoneNumbersNexmoProviderNotPassed() throws ClientProtocolException, IOException {
		providerName = "NEXMO";
		provider.setName("NEXMO");
		if(!skipNexmo){
			numberSearchService.searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType, pattern,
					nextPlivoIndex, nextNexmoIndex, markup);
			verify(nexmoFactoryImpl, times(1)).searchPhoneNumbers(Mockito.any(Provider.class), Mockito.any(ServiceConstants.class),
					Mockito.any(String.class), Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class)
					, Mockito.any(NumberResponse.class), Mockito.any(String.class));
		}
		else{
			numberSearchService.searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType, pattern,
					nextPlivoIndex, nextNexmoIndex, markup);
			verify(nexmoFactoryImpl, times(0)).searchPhoneNumbers(Mockito.any(Provider.class), Mockito.any(ServiceConstants.class),
					Mockito.any(String.class), Mockito.any(String.class),Mockito.any(String.class),Mockito.any(String.class)
					, Mockito.any(NumberResponse.class), Mockito.any(String.class));
		}		
	}	
}