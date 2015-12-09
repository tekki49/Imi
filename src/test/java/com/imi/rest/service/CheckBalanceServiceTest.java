package com.imi.rest.service;

import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.dao.ProviderDao;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Providercountry;
import com.imi.rest.model.BalanceResponse;

public class CheckBalanceServiceTest {
	
	@Mock
	Providercountry providerCountry;
	@Mock
	Provider provider;
	@Mock
	private Set<Providercountry> providerCountries;
	@Mock
	ProviderService providerService;
	@Mock
	PlivoFactoryImpl plivoFactoryImpl;
	@Mock
	NexmoFactoryImpl nexmoFactoryImpl;
	@Mock
	ProviderDao providerDao;
	@Mock
	BalanceResponse balanceResponse;

	@Test
	public void checkBalanceProviderPlivo() throws ClientProtocolException, IOException {
		providerCountries = new HashSet<Providercountry>(0);
		providerCountry=new Providercountry();
		providerService=Mockito.mock(ProviderService.class);
		plivoFactoryImpl =Mockito.mock(PlivoFactoryImpl.class);
		providerDao=new ProviderDao();
		Country country= new Country();
		country.setCountry("United States");
		providerService.setDao(providerDao);
		provider = new Provider( "apiKey","authId","PLIVO",providerCountries);
		providerCountry.setCountry(country);
		providerCountry.setId(1);
		providerCountry.setProvider(provider);
		providerCountry.setServices("services");
		providerCountries.add(providerCountry);
		balanceResponse = new BalanceResponse();
		balanceResponse.setAccountBalance("1000");
		doReturn(provider).when(providerService).getPlivioProvider();
		doReturn(balanceResponse).when(plivoFactoryImpl).checkBalance(provider);
		Assert.assertEquals("1000", plivoFactoryImpl.checkBalance(provider).getAccountBalance());
	}

	// Balance Check not required for TWILIO
	
/*	 * @Test public void checkBalanceProviderTwilio() throws
	 * ClientProtocolException, IOException { Provider provider =
	 * new Provider();provider.setName("TWILIO"); BalanceResponse
	 * balanceResponse = new BalanceResponse();
	 * balanceResponse.setAccountBalance("1000");
	 * when(providerService.getPlivioProvider()).thenReturn(provider);
	 * when(plivioFactoryImpl
	 * .checkBalance(providerService.getPlivioProvider())).thenReturn(
	 * balanceResponse); Assert.assertEquals("1000", twilioFactoryImpl
	 * .checkBalance(providerService.getTwilioProvider())); }*/
	 

	@Test
	public void checkBalanceProviderNexmo() throws ClientProtocolException, IOException {
		providerCountries = new HashSet<Providercountry>(0);
		providerCountry=new Providercountry();
		providerService=Mockito.mock(ProviderService.class);
		nexmoFactoryImpl =Mockito.mock(NexmoFactoryImpl.class);
		providerDao=new ProviderDao();
		Country country= new Country();
		country.setCountry("United States");
		providerService.setDao(providerDao);
		provider = new Provider( "apiKey","authId","NEXMO",providerCountries);
		providerCountry.setCountry(country);
		providerCountry.setId(1);
		providerCountry.setProvider(provider);
		providerCountry.setServices("services");
		providerCountries.add(providerCountry);
		balanceResponse = new BalanceResponse();
		balanceResponse.setAccountBalance("1000");
		doReturn(provider).when(providerService).getNexmoProvider();
		doReturn(balanceResponse).when(nexmoFactoryImpl).checkBalance(provider);
		Assert.assertEquals("1000", nexmoFactoryImpl.checkBalance(provider).getAccountBalance());
	}

}
