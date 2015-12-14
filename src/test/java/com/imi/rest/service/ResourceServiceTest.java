package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.dao.ChannelAssetsAllocationDao;
import com.imi.rest.dao.CountryDao;
import com.imi.rest.dao.ImiDefaultQueryDao;
import com.imi.rest.dao.NumbersReleasedDao;
import com.imi.rest.dao.ProvisioningDao;
import com.imi.rest.dao.PurchaseDao;
import com.imi.rest.dao.PurchaseHistoryDao;
import com.imi.rest.dao.ResourceAllocationDao;
import com.imi.rest.dao.ResourceMasterDao;
import com.imi.rest.dao.model.ChannelAssetsAllocation;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.NumbersReleased;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Providercountry;
import com.imi.rest.dao.model.Provisioning;
import com.imi.rest.dao.model.Purchase;
import com.imi.rest.dao.model.PurchaseHistory;
import com.imi.rest.dao.model.ResourceAllocation;
import com.imi.rest.dao.model.ResourceMaster;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.util.ImiHttpUtil;

public class ResourceServiceTest {

	@Mock
	ResourceMasterDao resourceMasterDao;
	@Mock
	ResourceAllocationDao resourceAllocationDao;
	@Mock
	ChannelAssetsAllocationDao channelAssetsAllocationDao;
	@Mock
	PurchaseDao purchaseDao;
	@Mock
	ProviderService providerService;
	@Mock
	CountryDao countryDao;
	@Mock
	ProvisioningDao provisioningDao;
	@Mock
	PurchaseHistoryDao purchaseHistoryDao;
	@Mock
	ImiDefaultQueryDao imiDefaultQueryDao;
	@Mock
	NumbersReleasedDao numbersReleasedDao;
	@Mock
	ImiHttpUtil imiHttpUtil;
	@InjectMocks
	ResourceService resourceService;

	
	Integer providerId;	
	Date date,maxDate,createdDate,updateDate;
	String endDate;
	String number; 
	PurchaseResponse purchaseResponse;
	ServiceConstants serviceTypeEnum; 
	Provider provider; 
	Country country; 
	Integer userid; 
	Integer clientId;
	Integer groupid; 
	Integer teamid; 
	String clientname; 
	String clientkey; 
	String teamuuid; 
	String uuid;
	String numberType;
	String restrictions;
	String namedQuery;
	ResourceMaster resourceMaster;
	ResourceAllocation resourceAllocation;
	ChannelAssetsAllocation channelAssetsAllocation;
	Providercountry providercountry;
	Purchase purchase;
	PurchaseHistory purchaseHistory;
	ApplicationResponse applicationResponse;
	Provisioning provisioning;
	GenericRestResponse restResponse;
	
	@Before
	public void setUp() throws ClientProtocolException, IOException {
		numberType="SMS";
		restrictions="restrictions";
		purchase = new Purchase();
		purchase.setMonthlyRentalRate("3.0");
		provider = new Provider();
		provider.setName("TWILIO");
		country = new Country();
		country.setCountryIso("US");
		providercountry = new Providercountry();
		country.setCountry("United States");
		providercountry.setResourceCountry(country);
		purchase.setNumberType(numberType);
		number = "123456789";
		userid = 123;
		clientId = 1234;
		groupid = 12345;
		teamid = 123456;
		clientname = "clientName";
		clientkey = "clientkey";
		teamuuid = "321";
		uuid = "321";
		purchaseResponse = new PurchaseResponse();
		resourceMaster = new ResourceMaster();
		resourceMaster.setResourceId(123);
		resourceMaster.setChannel((byte) 1);
		serviceTypeEnum = ServiceConstants.SMS;		
		namedQuery = "";		
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void updateResource() {
		List<Object> creditsResultList = new ArrayList<>();
		when(imiDefaultQueryDao.getNamedQueryResults(namedQuery)).thenReturn(creditsResultList);
		resourceService.updateResource(number, purchaseResponse, serviceTypeEnum, provider, country,
				userid, clientId, groupid, teamid, clientname, clientkey, teamuuid, numberType);
		verify(resourceMasterDao, times(1)).createNewResource(Mockito.any(ResourceMaster.class));
	}

	@Test
	public void updateResourceAllocation() {
		resourceService.updateResourceAllocation(resourceMaster, userid, clientId, groupid, teamid, clientname, clientkey);
		verify(resourceAllocationDao, times(1)).createNewResourceAllocation(Mockito.any(ResourceAllocation.class));
	}
	@Test
	public void updateChannelAssetsAllocation() {
		resourceService.updateChannelAssetsAllocation(resourceMaster, userid, clientId, groupid, teamid, clientname, clientkey);
		verify(channelAssetsAllocationDao, times(1)).createNewChannelAssetsAllocation(Mockito.any(ChannelAssetsAllocation.class));
	}
	@Test
	public void updatePurchase()  {
		resourceMaster.setProviderId(1);
		resourceMaster.setCountryIso("countryIso");
		resourceService.updatePurchase(purchaseResponse, numberType, restrictions, resourceMaster);
		verify(purchaseDao, times(1)).createNewPurchase(Mockito.any(Purchase.class));
	}
	@Test
	public void updatepurchaseHistory() {
		resourceService.updatePurchasehistory(purchase);
		verify(purchaseHistoryDao, times(1)).createNewPurchaseHistory(Mockito.any(PurchaseHistory.class));
	}	
	@Test
	public void provisionData() {
		purchaseHistory = new PurchaseHistory();
		purchaseHistory.setNumber(number);
		applicationResponse=new ApplicationResponse();
		applicationResponse.setSmsFallbackMethod("getSmsFallbackMethod");
		applicationResponse.setSms_fallback_url("smsFallBackUrl");
		when(purchaseHistoryDao.getPurchaseHistoryByNumber(number)).thenReturn(purchaseHistory);
		resourceService.provisionData(number, applicationResponse);
		Provisioning provisioning = purchaseHistory.getNumberProvisioning();
		verify(provisioningDao, times(1)).updateProvisioning(provisioning);
		verify(purchaseHistoryDao, times(1)).createNewPurchaseHistory(purchaseHistory);
		assertEquals(provisioning.getSmsFallbackMethod(), applicationResponse.getSmsFallbackMethod());
		assertEquals(provisioning.getSmsFallbackUrl(), applicationResponse.getSmsFallbackUrl());
	}
	@Test
	public void getResourceMasterByNumber() {
		resourceMaster.setVoiceInboundPrice("1.00");
		number="123456789";
		providerId = 1;
		provider.setId(providerId);
		doReturn(resourceMaster).when(resourceMasterDao).getResourceByNumber(number, providerId);
		ResourceMaster returnedResourceMaster = resourceService.getResourceMasterByNumberAndProvider(number, provider); 
		assertEquals("1.00", resourceMaster.getVoiceInboundPrice());
	}
	@Test
	public void deleteResourceMaster() {
		resourceService.deleteResourceMaster(resourceMaster);
		verify(resourceMasterDao, times(1)).deleteResourceMaster(resourceMaster);
	}
	@Test
	public void deleteResourceAllocation() {
		resourceMaster.setResourceId(123);
		resourceAllocation = new ResourceAllocation();
		resourceAllocation.setResourceId(resourceMaster.getResourceId());
		doReturn(resourceAllocation).when(resourceAllocationDao).getResourceAllocationById(resourceMaster.getResourceId());
		resourceService.deleteResourceAllocation(resourceMaster);
		verify(resourceAllocationDao, times(1)).deleteResourceAllocation(resourceAllocation);
		assertEquals(123, resourceAllocation.getResourceId());
	}
	@Test
	public void deleteChannelAssetsAllocation() {
		resourceMaster.setResourceId(123);
		channelAssetsAllocation = new ChannelAssetsAllocation();
		channelAssetsAllocation.setChannelId(1000);
		doReturn(channelAssetsAllocation).when(channelAssetsAllocationDao).getChannelAssetsAllocationByAssetId(resourceMaster.getResourceId());
		resourceService.deleteChannelAssetsAllocation(resourceMaster);
		verify(channelAssetsAllocationDao, times(1)).deleteChannelAssetsAllocation(channelAssetsAllocation);
		assertEquals(Integer.valueOf(1000), channelAssetsAllocation.getChannelId());
	}
	@Test
	public void updateNewNumbersReleased() {
		resourceMaster.setCreatedOn(new Date());
		resourceService.updateNewNumbersReleased(resourceMaster, clientkey);
		verify(numbersReleasedDao, times(1)).createNewNumbersReleased(Mockito.any(NumbersReleased.class));
	}
	@Test
	public void updateCreditsConfigResource() throws ParseException, IOException {
//		restResponse = new GenericRestResponse();
//		String voiceRateCredits = "123";
//		doReturn(restResponse).when(imiHttpUtil).defaultHttpPostHandler(Mockito.anyString(), Mockito.anyMap(), Mockito.anyString());
//		resourceService.updateCreditsConfigResource(number, uuid, voiceRateCredits);
//		verify(imiHttpUtil, times(1)).defaultHttpPostHandler(Mockito.anyString(), Mockito.anyMap(), Mockito.anyString());
	}
	@Test
	public void deletePurchase() {
		resourceMaster.setServiceCode("100");
		doReturn(purchase).when(purchaseDao).getPurchaseByNumber(resourceMaster.getServiceCode());
		resourceService.deletePurchase(resourceMaster);
		verify(purchaseDao, times(1)).deletePurchase(purchase);
	}
}
