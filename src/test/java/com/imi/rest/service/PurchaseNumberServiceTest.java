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
import org.mockito.Spy;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Providercountry;
import com.imi.rest.dao.model.Purchase;
import com.imi.rest.dao.model.PurchaseHistory;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.PurchaseRequest;
import com.imi.rest.model.PurchaseResponse;

public class PurchaseNumberServiceTest {
	@Mock
	PlivoFactoryImpl plivioFactoryImpl;
	@Mock
	NexmoFactoryImpl nexmoFactoryImpl;
	@Mock 
	TwilioFactoryImpl twilioFactoryImpl;
	@InjectMocks
	PurchaseNumberService purchaseNumberService;
	
	private String number,numberType,services,clientname,clientkey,teamuuid,providerName;
	private Integer userid,clientId,groupid,teamid;
	Providercountry providerCountry;
	private Set<Providercountry> providercountries;
	private Set<PurchaseHistory> purchasehistories;
	private Set<Purchase> purchases;
	Country country;
	ServiceConstants serviceTypeEnum;
	PurchaseResponse purchaseResponse;
	PurchaseRequest purchaseRequest;
	Provider provider;

	@Before
	public void setup() {
		providerCountry = new Providercountry();
		providercountries = new HashSet<Providercountry>(0);
		purchasehistories = new HashSet<PurchaseHistory>(0);
		purchases = new HashSet<Purchase>(0);
		purchaseResponse = new PurchaseResponse();
		purchaseRequest=new PurchaseRequest();
		services="SERVICES";
		clientname="CLIENT_NAME";
		clientkey="CLIENT_KEY";
		teamuuid="TEAM_UID";
		userid=103;
		clientId=102;
		groupid=101;
		teamid=100;
		country = new Country("US", "United States", "1", providercountries);
		providerCountry = new Providercountry(provider, country, services, purchases, purchasehistories);
		number = "123456789";
		numberType = "MOBILE";
		serviceTypeEnum = ServiceConstants.SMS;
		providercountries.add(providerCountry);
		purchaseRequest.setNumber(number);
		purchaseResponse.setNumber("123456789");
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void purchaseNumberWhenProviderTwilio() throws ClientProtocolException, IOException {
		providerName="TWILIO";
		provider = new Provider("apiKey", "authId",providerName, providercountries);
		doReturn(purchaseResponse).when(twilioFactoryImpl).purchaseNumber(number, numberType, provider, country,
				serviceTypeEnum, userid, clientId, groupid, teamid, clientname, clientkey, purchaseRequest, teamuuid);
		PurchaseResponse purchaseResponseReturnValue=purchaseNumberService.purchaseNumber(number, numberType, provider, country, serviceTypeEnum, userid, clientId, groupid, teamid, clientname, clientkey, purchaseRequest, teamuuid);
		assertNotNull(purchaseResponseReturnValue);
		assertEquals(number,purchaseResponseReturnValue.getNumber());
	}

	@Test
	public void purchaseNumberWhenProviderPlivo() throws ClientProtocolException, IOException {
		providerName="PLIVO";
		provider = new Provider("apiKey", "authId",providerName, providercountries);
		doReturn(purchaseResponse).when(plivioFactoryImpl).purchaseNumber(number, numberType, provider, country,
				serviceTypeEnum, userid, clientId, groupid, teamid, clientname, clientkey, purchaseRequest, teamuuid);
		PurchaseResponse purchaseResponseReturnValue=purchaseNumberService.purchaseNumber(number, numberType, provider, country, serviceTypeEnum, userid, clientId, groupid, teamid, clientname, clientkey, purchaseRequest, teamuuid);
		assertNotNull(purchaseResponseReturnValue);
		assertEquals(number,purchaseResponseReturnValue.getNumber());
	}

	@Test
	public void purchaseNumberWhenProviderNexmo() throws ClientProtocolException, IOException {
		providerName="NEXMO";
		provider = new Provider("apiKey", "authId",providerName, providercountries);
		doReturn(purchaseResponse).when(nexmoFactoryImpl).purchaseNumber(number, numberType, provider, country,
				serviceTypeEnum, userid, clientId, groupid, teamid, clientname, clientkey, purchaseRequest, teamuuid);
		PurchaseResponse purchaseResponseReturnValue=purchaseNumberService.purchaseNumber(number, numberType, provider, country, serviceTypeEnum, userid, clientId, groupid, teamid, clientname, clientkey, purchaseRequest, teamuuid);
		assertNotNull(purchaseResponseReturnValue);
		assertEquals(number,purchaseResponseReturnValue.getNumber());
	}
	
	public void purchaseNumberWhenProviderNeither() throws ClientProtocolException, IOException {
		providerName="NONE";
		provider = new Provider();
		provider.setName(providerName);
		try{
			purchaseNumberService.purchaseNumber(number, numberType, provider, country, serviceTypeEnum, userid, clientId, groupid, teamid, clientname, clientkey, purchaseRequest, teamuuid);
		}
		catch(InboundRestException e){
			assertEquals("Provider " + provider.getName() + " is invalid", e.getDetailedMessage());
			assertEquals(InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION.getCode(), e.getCode());
		}
	}
}
