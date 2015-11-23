package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Providercountry;
import com.imi.rest.dao.model.Purchase;
import com.imi.rest.dao.model.Purchasehistory;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.PurchaseResponse;

public class PurchaseNumberServiceTest {
	@Mock
	PlivoFactoryImpl plivioFactoryImpl = new PlivoFactoryImpl();
	@Mock
	NexmoFactoryImpl nexmoFactoryImpl = new NexmoFactoryImpl();
	// @Mock TwilioFactoryImpl twilioFactoryImpl=new TwilioFactoryImpl();
	@Mock
	private String number;
	@Mock
	private String numberType;
	@Mock
	private String services;
	@Mock
	Providercountry providerCountry = new Providercountry();
	@Mock
	private Set<Providercountry> providercountries = new HashSet<Providercountry>(0);
	@Mock
	private Set<Purchasehistory> purchasehistories = new HashSet<Purchasehistory>(0);
	@Mock
	private Set<Purchase> purchases;
	@Mock
	Provider provider;
	@Mock
	Country country;
	@Spy
	ServiceConstants serviceTypeEnum;
	@Mock
	PurchaseResponse purchaseResponse = new PurchaseResponse();

	@Before
	public void setup() {
		provider = new Provider("apiKey", "authId", "name", providercountries);
		country = new Country("US", "United States", "1", providercountries);
		providerCountry = new Providercountry(country, provider, services, purchases, purchasehistories);
		number = "123456789";
		numberType = "MOBILE";
	}

	@Test
	public void purchaseNumberWhenProviderTwilio() throws ClientProtocolException, IOException, ImiException {
		serviceTypeEnum = ServiceConstants.SMS;
		providercountries.add(providerCountry);
		purchaseResponse = new PurchaseResponse();
		purchaseResponse.setNumber("123456789");
		TwilioFactoryImpl twilioFactoryImpl = Mockito.mock(TwilioFactoryImpl.class);
		doReturn(purchaseResponse).when(twilioFactoryImpl).purchaseNumber(number, numberType, provider, country,
				serviceTypeEnum);
		assertEquals(purchaseResponse.getNumber(), number);
	}

	@Test
	public void purchaseNumberWhenProviderPlivo() throws ClientProtocolException, IOException, ImiException {
		serviceTypeEnum = ServiceConstants.SMS;
		providercountries.add(providerCountry);
		purchaseResponse = new PurchaseResponse();
		purchaseResponse.setNumber("123456789");
		PlivoFactoryImpl plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		doReturn(purchaseResponse).when(plivoFactoryImpl).purchaseNumber(number, numberType, provider, country,
				serviceTypeEnum);
		assertEquals(purchaseResponse.getNumber(), number);
	}

	@Test
	public void purchaseNumberWhenProviderNexmo() throws ClientProtocolException, IOException, ImiException {
		serviceTypeEnum = ServiceConstants.SMS;
		purchaseResponse = new PurchaseResponse();
		purchaseResponse.setNumber("123456789");
		NexmoFactoryImpl nexmoFactoryImpl = Mockito.mock(NexmoFactoryImpl.class);
		doReturn(purchaseResponse).when(nexmoFactoryImpl).purchaseNumber(number, numberType, provider, country,
				serviceTypeEnum);
		assertEquals(purchaseResponse.getNumber(), number);

	}
}
