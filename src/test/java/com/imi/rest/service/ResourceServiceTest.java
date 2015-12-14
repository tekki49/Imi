package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.dao.ChannelAssetsAllocationDao;
import com.imi.rest.dao.CountryDao;
import com.imi.rest.dao.ProvisioningDao;
import com.imi.rest.dao.PurchaseDao;
import com.imi.rest.dao.PurchaseHistoryDao;
import com.imi.rest.dao.ResourceAllocationDao;
import com.imi.rest.dao.ResourceMasterDao;
import com.imi.rest.dao.model.ChannelAssetsAllocation;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Providercountry;
import com.imi.rest.dao.model.Provisioning;
import com.imi.rest.dao.model.Purchase;
import com.imi.rest.dao.model.PurchaseHistory;
import com.imi.rest.dao.model.ResourceAllocation;
import com.imi.rest.dao.model.ResourceMaster;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.PurchaseResponse;

public class ResourceServiceTest {

	@Mock
	ResourceMasterDao resourceMasterDao;
	@Mock
	ResourceAllocationDao resourceAllocationDao;
	@Mock
	ResourceAllocation resourceAllocation;
	@Mock
	ChannelAssetsAllocationDao channelAssetsAllocationDao;
	@Mock
	ApplicationResponse applicationResponse;
	@Mock
	ChannelAssetsAllocation channelAssetsAllocation;
	@Mock
	PurchaseDao purchaseDao;
	@Mock
	ProviderService providerService;
	@Mock
	PurchaseResponse purchaseResponse;
	@Mock
	CountryDao countryDao;
	@Mock
	Country country;
	@Mock
	Providercountry providercountry;
	@Mock
	Provider provider;
	@Mock
	ProvisioningDao provisioningDao;
	@Mock
	PurchaseHistory purchasehistory;
	@Mock
	PurchaseHistoryDao purchaseHistoryDao;
	@Mock
	Purchase purchase;
	@Mock
	Provisioning provisioning;
	@Spy
	ResourceMaster resourceMaster;
	@Mock
	String number,numberType,restrictions;
	@Mock
	Integer providerId;
	@Mock
	ServiceConstants serviceTypeEnum;
	@Mock
	Date date,maxDate,createdDate,updateDate;
	@Mock
	String endDate;
	
	@Test
	public void updateResource() {
			resourceMasterDao=Mockito.mock(ResourceMasterDao.class);
			serviceTypeEnum=ServiceConstants.SMS;
			resourceMaster = new ResourceMaster();
			resourceMaster.setServiceCode(number);
			resourceMaster.setChannel((byte)1);
			resourceMaster.setResourceType((byte) 1);
			resourceMaster.setCategory((byte) 2);
			resourceMaster.setSubCategory((byte) 0);
			resourceMaster.setCreatedOn(new Date());
			doNothing().when(resourceMasterDao).createNewResource(resourceMaster);
	}

	@Test
	public void updateResourceAllocation() {
			resourceAllocationDao = Mockito.mock(ResourceAllocationDao.class);
			resourceAllocation=new ResourceAllocation();
			resourceAllocation.setResourceId(1);
			resourceAllocation.setCreatedOn(new Date());
			resourceAllocation.setNodeId("OH#UMPDEV");
			resourceAllocation.setNodeType("OH");
			resourceAllocation.setStatus((byte) 2);
			resourceAllocation.setUpdatedOn(date);
			resourceAllocation.setRcId(null);
			resourceAllocation.setExpiresOn(maxDate);
			resourceAllocation.setActivatedOn(createdDate);
			resourceAllocation.setCreatedBy("Admin");
			resourceAllocation.setIsDefault((byte) 2);
			doNothing().when(resourceAllocationDao).createNewResourceAllocation(resourceAllocation);
	}
	@Test
	public void updateChannelAssetsAllocation() {
			channelAssetsAllocationDao=Mockito.mock(ChannelAssetsAllocationDao.class);
			channelAssetsAllocation = new ChannelAssetsAllocation();
			channelAssetsAllocation.setAssetId(1);
			channelAssetsAllocation.setKeywordId(0L);
			channelAssetsAllocation.setChannelId(1);
			channelAssetsAllocation.setIsDefault((byte) 1);
			channelAssetsAllocation.setShareType((byte) 0);
			channelAssetsAllocation.setAssetType(2);
			channelAssetsAllocation.setCreatedOn(createdDate);
			channelAssetsAllocation.setUpdatedOn(updateDate);
			doNothing().when(channelAssetsAllocationDao).createNewChannelAssetsAllocation(channelAssetsAllocation);
	}
	@Test
	public void updatePurchase()  {
			purchaseDao=Mockito.mock(PurchaseDao.class);
			numberType="SMS";
			restrictions="restrictions";
			String timeStamp="2015-11-20 20:12:23.234";
			Purchase purchase = new Purchase();
			purchase.setMonthlyRentalRate("3.0");
			Provider provider = new Provider();
			provider.setName("TWILIO");
			Country country = new Country();
			country.setCountryIso("US");
			providercountry = new Providercountry();
			country.setCountry("United States");
			providercountry.setResourceCountry(country);
			purchase.setNumberType(numberType);
			int number = 123456789;
			purchaseResponse=new PurchaseResponse();	
			resourceMaster = new ResourceMaster();
			purchaseResponse.setSetUpRate("2.05");
			purchaseResponse.setSmsRate("3.25");
			purchaseResponse.setVoicePrice("4.56");
			resourceMaster.setResourceId(1);
			purchase.setNumber(""+number);
			purchase.setResouceManagerId(resourceMaster.getResourceId());
			purchase.setEffectiveDate(timeStamp);
			purchase.setRestrictions(restrictions);
			purchase.setSetUpRate(purchaseResponse.getSetUpRate());
			purchase.setSmsRate(purchaseResponse.getSmsRate());
			purchase.setVoicePrice(purchaseResponse.getVoicePrice());
			purchase.setNumberProviderCountry(providercountry);
			doNothing().when(purchaseDao).createNewPurchase(purchase);
	}
	@Test
	public void updatePurchasehistory() {
			purchaseHistoryDao=Mockito.mock(PurchaseHistoryDao.class);
			purchasehistory = new PurchaseHistory();
			purchase=new Purchase();
			country=new Country();
			country.setCountry("United States");
			providercountry =new Providercountry();
			providercountry.setResourceCountry(country);
			purchase.setMonthlyRentalRate("1.00");
			purchase.setNumber(""+123456789);
			purchase.setNumberProviderCountry(providercountry);
			purchase.setResouceManagerId(1);
			purchase.setRestrictions("restrictions");
			purchase.setSetUpRate("2.00");
			purchase.setSmsRate("3.00");
			purchase.setEffectiveDate("2015/11/20");
			purchase.setVoicePrice("4.00");
			purchasehistory.setEndDate(endDate);
			purchasehistory.setMonthlyRentalRate(purchase.getMonthlyRentalRate());
			purchasehistory.setNumber(purchase.getNumber());
			purchasehistory.setNumberType(purchase.getNumberType());
			purchasehistory.setNumberProviderCountry(purchase.getNumberProviderCountry());
			purchasehistory.setResourceManagerId(purchase.getResouceManagerId());
			purchasehistory.setRestrictions(purchase.getRestrictions());
			purchasehistory.setSetUpRate(purchase.getSetUpRate());
			purchasehistory.setSmsPrice(purchase.getSmsRate());
			purchasehistory.setStartDate(purchase.getEffectiveDate());
			purchasehistory.setVoicePrice(purchase.getVoicePrice());
			doNothing().when(purchaseHistoryDao).createNewPurchaseHistory(purchasehistory);
	}
	@Test
	public void getResourceMasterByNumber() {
		resourceMasterDao=Mockito.mock(ResourceMasterDao.class);
		resourceMaster=new ResourceMaster();
		resourceMaster.setVoiceInboundPrice("1.00");
		number="123456789";
		providerId = 1;
		doReturn(resourceMaster).when(resourceMasterDao).getResourceByNumber(number, providerId);
		assertEquals("1.00", resourceMaster.getVoiceInboundPrice());
	}
	@Test
	public void provisionData() {
			provisioningDao=Mockito.mock(ProvisioningDao.class);
			provisioning = new Provisioning();
			applicationResponse=new ApplicationResponse();
			applicationResponse.setSmsFallbackMethod("getSmsFallbackMethod");
			applicationResponse.setSms_fallback_url("smsFallBackUrl");
			applicationResponse.setSmsMethod("SmsMethod");
			applicationResponse.setSmsStatusCallback("SmsStatusCallback");
			applicationResponse.setSmsUrl(".getSmsUrl");
			applicationResponse.setStatusCallback("StatusCallback  ");
			applicationResponse.setStatusCallbackMethod("StatusCallbackMethod");
			applicationResponse. setVoiceFallbackMethod("VoiceFallbackMethod");
			applicationResponse.setVoiceFallback("VoiceFallback");
			applicationResponse.setVoiceUrl("VoiceUrl");
			provisioning.setSmsFallbackMethod(applicationResponse.getSmsFallbackMethod());
			provisioning.setSmsFallbackUrl(applicationResponse.getSmsFallbackUrl());
			provisioning.setSmsMethod(applicationResponse.getSmsMethod());
			provisioning.setSmsStatusCallback(applicationResponse.getSmsStatusCallback());
			provisioning.setSmsUrl(applicationResponse.getSmsUrl());
			provisioning.setStatusCallBack(applicationResponse.getStatusCallback());
			provisioning.setStatusCallbackMethod(applicationResponse.getStatusCallbackMethod());
			provisioning.setVoiceFallbackMethod(applicationResponse.getVoiceFallbackMethod());
			provisioning.setVoiceFallbackUrl(applicationResponse.getVoiceFallback());
			provisioning.setVoiceUrl(applicationResponse.getVoiceUrl());
			doNothing().when(provisioningDao).updateProvisioning(provisioning);
	}
}
