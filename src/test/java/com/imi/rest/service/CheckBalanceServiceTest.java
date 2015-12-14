package com.imi.rest.service;

import static org.mockito.Mockito.doReturn;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.BalanceResponse;

public class CheckBalanceServiceTest {
	@Mock
	ProviderService providerService;
	@Mock
	PlivoFactoryImpl plivoFactoryImpl;
	@Mock
	NexmoFactoryImpl nexmoFactoryImpl;
	@InjectMocks
	CheckBalanceService checkBalanceService;
	
	Provider provider;
	BalanceResponse balanceResponse;
	Country country;
	String providerName;
	
	@Before
	public void setUp() throws ClientProtocolException, IOException {
		balanceResponse = new BalanceResponse();
		balanceResponse.setAccountBalance("1000");
		providerService=Mockito.mock(ProviderService.class);
		plivoFactoryImpl =Mockito.mock(PlivoFactoryImpl.class);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void checkBalanceForProviderPlivoTest() throws ClientProtocolException, IOException {
		providerName = "PLIVO";
		provider = new Provider( "apiKey","authId","PLIVO",null);
		doReturn(provider).when(providerService).getPlivioProvider();		
		doReturn(balanceResponse).when(plivoFactoryImpl).checkBalance(provider);
		BalanceResponse balance = checkBalanceService.checkBalance(providerName);
		Assert.assertEquals("PLIVO", provider.getName());
		Assert.assertEquals("apiKey", provider.getApiKey());
		Assert.assertEquals("authId", provider.getAuthId());
		Assert.assertEquals("1000", balance.getAccountBalance());
	}
	
	@Test
	public void checkBalanceForProviderNexmoTest() throws ClientProtocolException, IOException {
		providerName = "NEXMO";
		provider = new Provider( "apiKey","authId","NEXMO",null);
		doReturn(provider).when(providerService).getNexmoProvider();		
		doReturn(balanceResponse).when(nexmoFactoryImpl).checkBalance(provider);
		BalanceResponse balance = checkBalanceService.checkBalance(providerName);
		Assert.assertEquals("NEXMO", provider.getName());
		Assert.assertEquals("apiKey", provider.getApiKey());
		Assert.assertEquals("authId", provider.getAuthId());
		Assert.assertEquals("1000", balance.getAccountBalance());
	}

	// Balance Check not available for TWILIO	
	@Test 
	public void checkBalanceForProviderTwilioTest() throws  ClientProtocolException, IOException { 
		providerName = "TWILIO";
		provider = new Provider( "apiKey","authId","TWILIO",null);
		doReturn(provider).when(providerService).getTwilioProvider();	
		try{
			checkBalanceService.checkBalance(providerName);
		}
		catch(InboundRestException e){
			Assert.assertEquals("Provider TWILIO  does not support Check Balance api", e.getDetailedMessage());
		}		
		Assert.assertEquals("TWILIO", provider.getName());
		Assert.assertEquals("apiKey", provider.getApiKey());
		Assert.assertEquals("authId", provider.getAuthId());
	}
	@Test 
	public void checkBalanceForInvalidProviderTest() throws  ClientProtocolException, IOException { 
		providerName = "InvalidProvider";
		try{
			checkBalanceService.checkBalance(providerName);
		}
		catch(InboundRestException e){
			Assert.assertEquals("Provider InvalidProvider is invalid", e.getDetailedMessage());
		}		
	}

}
