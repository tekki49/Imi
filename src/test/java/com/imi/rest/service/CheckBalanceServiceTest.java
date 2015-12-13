package com.imi.rest.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
import com.imi.rest.dao.ProviderDao;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Providercountry;
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
	
	Providercountry providerCountry;
	Provider provider;
	private Set<Providercountry> providerCountries;
	ProviderDao providerDao;
	BalanceResponse balanceResponse;
	String providerName;
	
	@Before
	public void setUp(){
		providerCountries = new HashSet<Providercountry>(0);
		providerCountry=new Providercountry();
		providerDao=new ProviderDao();
		Country country= new Country();
		country.setCountry("United States");
		providerCountry.setResourceCountry(country);
		balanceResponse = new BalanceResponse();
		balanceResponse.setAccountBalance("1000");
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void checkBalanceProviderPlivo() throws ClientProtocolException, IOException {
		providerName="PLIVO";
		provider = new Provider( "apiKey","authId",providerName,providerCountries);
		providerCountry.setNumberProvider(provider);
		providerCountries.add(providerCountry);
		doReturn(provider).when(providerService).getPlivioProvider();
		doReturn(balanceResponse).when(plivoFactoryImpl).checkBalance(provider);
		BalanceResponse balanceResponseReturnValue=checkBalanceService.checkBalance(providerName);
		assertNotNull(balanceResponseReturnValue);
		assertEquals("1000",balanceResponseReturnValue.getAccountBalance());
	}

	@Test(expected=InboundRestException.class)
	public void checkBalanceProviderTwilio() throws ClientProtocolException, IOException{
		providerName="TWILIO";
		checkBalanceService.checkBalance(providerName);
	}

	@Test
	public void checkBalanceProviderNexmo() throws ClientProtocolException, IOException {
		providerName="NEXMO";
		provider = new Provider( "apiKey","authId",providerName,providerCountries);
		providerCountry.setNumberProvider(provider);
		providerCountries.add(providerCountry);
		doReturn(provider).when(providerService).getNexmoProvider();
		doReturn(balanceResponse).when(nexmoFactoryImpl).checkBalance(provider);
		BalanceResponse balanceResponseReturnValue=checkBalanceService.checkBalance(providerName);
		assertNotNull(balanceResponseReturnValue);
		assertEquals("1000",balanceResponseReturnValue.getAccountBalance());
	}

}
