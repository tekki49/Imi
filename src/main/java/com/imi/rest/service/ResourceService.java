package com.imi.rest.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
import com.imi.rest.util.ImiDataFormatUtils;
import com.imi.rest.util.ImiHttpUtil;

import org.apache.http.entity.ContentType;

@Service
public class ResourceService {

	@Autowired
	ResourceMasterDao resourceMasterDao;

	@Autowired
	ResourceAllocationDao resourceAllocationDao;

	@Autowired
	ChannelAssetsAllocationDao channelAssetsAllocationDao;

	@Autowired
	PurchaseDao purchaseDao;

	@Autowired
	ProviderService providerService;

	@Autowired
	CountryDao countryDao;

	@Autowired
	ProvisioningDao provisioningDao;

	@Autowired
	PurchaseHistoryDao purchaseHistoryDao;

	@Autowired
	NumbersReleasedDao numbersReleasedDao;

	@Autowired
	ImiDefaultQueryDao defaultQueryDao;

	@Value(value = "${credits.config.query}")
	String creditsConfigQuery;

	@Value("${resource.type.config}")
	Integer resourceType;

	@Value("${credits.config.resource.url}")
	String creditsResourceConfigUrl;

	@Value("${credits.config.resource.authkey}")
	String creditsResourceAuthkey;

	@Value("${credits.config.resource.request}")
	String creditsConfigResourceRequest;

	private static final Logger LOG = Logger.getLogger(ResourceService.class);

	public ResourceMaster updateResource(String number, PurchaseResponse purchaseResponse,
			ServiceConstants serviceTypeEnum, Provider provider, Country country, Integer userid, Integer clientId,
			Integer groupid, Integer teamid, String clientname, String clientkey, String teamuuid, String numberType) {
		ResourceMaster resourceMaster = new ResourceMaster();
		try {
			resourceMaster.setServiceCode(number);
			// may need to configure
			resourceMaster.setChannel(ImiDataFormatUtils.getChannel(serviceTypeEnum));
			List<Object> creditsResultList = defaultQueryDao.getNamedQueryResults(creditsConfigQuery);
			BigInteger creditsMultiplier = new BigInteger("1");
			if (creditsResultList.size() > 0) {
				creditsMultiplier = (BigInteger) creditsResultList.get(0);
			}
			// hardcoding need to configure
			int resourceType = 4;
			resourceMaster.setResourceType((byte) resourceType);
			resourceMaster.setCategory((byte) 2);
			resourceMaster.setSubCategory((byte) 0);
			resourceMaster.setProviderId(provider.getId());
			resourceMaster.setNumberType(numberType);
			// hardcoding 2,5 for now need to configure
			String monthlyRentalRate = purchaseResponse.getMonthlyRentalRate() == null
					|| purchaseResponse.getMonthlyRentalRate().trim().equals("") ? "0"
							: ImiDataFormatUtils.roundOffToN(purchaseResponse.getMonthlyRentalRate(), 2);
			monthlyRentalRate = ImiDataFormatUtils.multiply(monthlyRentalRate, creditsMultiplier.toString());
			monthlyRentalRate = ImiDataFormatUtils.roundOffToN(monthlyRentalRate, 0);
			resourceMaster.setMonthlyRentalPrice(monthlyRentalRate);
			String voiceRate = purchaseResponse.getVoicePrice() == null
					|| purchaseResponse.getVoicePrice().trim().equals("") ? "0"
							: ImiDataFormatUtils.roundOffToN(purchaseResponse.getVoicePrice(), 5);
			voiceRate = ImiDataFormatUtils.multiply(voiceRate, creditsMultiplier.toString());
			voiceRate = ImiDataFormatUtils.roundOffToN(voiceRate, 0);
			resourceMaster.setVoiceInboundPrice(voiceRate);
			String smsRate = purchaseResponse.getSmsRate() == null || purchaseResponse.getSmsRate().trim().equals("")
					? "0" : ImiDataFormatUtils.roundOffToN(purchaseResponse.getSmsRate(), 5);
			smsRate = ImiDataFormatUtils.multiply(smsRate, creditsMultiplier.toString());
			smsRate = ImiDataFormatUtils.roundOffToN(smsRate, 0);
			resourceMaster.setSmsInboundPrice(smsRate);
			updateCreditsConfigResource(number, teamuuid, monthlyRentalRate);
			resourceMaster.setCountryCode(country.getCountryCode());
			resourceMaster.setCountryIso(country.getCountryIso());
			resourceMaster.setCreatedOn(new Date());
			resourceMasterDao.createNewResource(resourceMaster);
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
		return resourceMaster;
	}

	public void updateResourceAllocation(ResourceMaster resourceMaster, Integer userid, Integer clientId,
			Integer groupid, Integer teamid, String clientname, String clientkey) {
		ResourceAllocation resourceAllocation = new ResourceAllocation();
		try {
			resourceAllocation.setResourceId(resourceMaster.getResourceId());
			resourceAllocation.setCreatedOn(new Date());
			// Hardcoding OH for now change to properties file
			String nodeType = "OH";
			String admin = "Admin";
			int rcId = 21;
			resourceAllocation.setNodeId(nodeType + "#" + clientname);
			resourceAllocation.setRcId(rcId);
			resourceAllocation.setNodeType(nodeType);
			resourceAllocation.setStatus((byte) 2);
			resourceAllocation.setUpdatedOn(resourceAllocation.getCreatedOn());
			resourceAllocation.setExpiresOn(ImiDataFormatUtils.getmaxDate());
			resourceAllocation.setActivatedOn(resourceAllocation.getCreatedOn());
			resourceAllocation.setCreatedBy(admin);
			resourceAllocation.setIsDefault((byte) 2);
			resourceAllocation.setUserkey(clientkey);
			resourceAllocationDao.createNewResourceAllocation(resourceAllocation);
		} catch (Exception e) {
		}
	}

	public void updateChannelAssetsAllocation(ResourceMaster resourceMaster, Integer userid, Integer clientId,
			Integer groupid, Integer teamid, String clientname, String clientkey) {
		try {
			ChannelAssetsAllocation channelAssetsAllocation = new ChannelAssetsAllocation();
			channelAssetsAllocation.setAssetId(resourceMaster.getResourceId());
			channelAssetsAllocation.setKeywordId(0L);
			channelAssetsAllocation.setChannelId(Integer.parseInt(String.valueOf(resourceMaster.getChannel())));
			channelAssetsAllocation.setIsDefault((byte) 1);
			channelAssetsAllocation.setShareType((byte) 0);
			channelAssetsAllocation.setStatus(1);
			channelAssetsAllocation.setAssetType(2);
			channelAssetsAllocation.setCreatedOn(new Date());
			channelAssetsAllocation.setUpdatedOn(new Date());
			channelAssetsAllocation.setClientId(clientId);
			channelAssetsAllocation.setUserId(new Long(userid));
			channelAssetsAllocation.setGroupId(new Long(groupid));
			channelAssetsAllocation.setTeamId(new Long(teamid));
			// channelAssetsAllocation.setCreatedBy(createdBy);
			channelAssetsAllocationDao.createNewChannelAssetsAllocation(channelAssetsAllocation);
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
	}

	public Purchase updatePurchase(PurchaseResponse purchaseResponse, String numberType, String restrictions,
			ResourceMaster resourceMaster) {
		Purchase purchase = new Purchase();
		try {
			purchase.setMonthlyRentalRate(resourceMaster.getMonthlyRentalPrice());
			Provider provider = providerService.getProviderById(resourceMaster.getProviderId());
			Country country = countryDao.getCountryByIso(resourceMaster.getCountryIso());
			Providercountry providercountry = countryDao.getProviderCountryByCountryAndProvider(country, provider);
			purchase.setNumberType(numberType);
			purchase.setNumber(resourceMaster.getServiceCode());
			purchase.setResouceManagerId(resourceMaster.getResourceId());
			purchase.setEffectiveDate(ImiDataFormatUtils.getCurrentTimeStamp());
			purchase.setRestrictions(restrictions);
			purchase.setSetUpRate(purchaseResponse.getSetUpRate());
			purchase.setSmsRate(resourceMaster.getSmsInboundPrice());
			purchase.setVoicePrice(resourceMaster.getVoiceInboundPrice());
			purchase.setNumberProviderCountry(providercountry);
			purchaseDao.createNewPurchase(purchase);
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
		return purchase;
	}

	public PurchaseHistory updatePurchasehistory(Purchase purchase) {
		PurchaseHistory purchasehistory = new PurchaseHistory();
		try {
			purchasehistory.setEndDate(ImiDataFormatUtils.getmaxDateString());
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
			purchaseHistoryDao.createNewPurchaseHistory(purchasehistory);
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
		return purchasehistory;
	}

	public void provisionData(String number, ApplicationResponse applicationResponse) {
		try {
			PurchaseHistory purchaseHistory = purchaseHistoryDao.getPurchaseHistoryByNumber(number);
			Provisioning provisioning = new Provisioning();
			provisioning.setSmsFallbackMethod(applicationResponse.getSmsFallbackMethod());
			provisioning.setSmsFallbackUrl(applicationResponse.getSmsFallbackUrl());
			provisioning.setSmsMethod(applicationResponse.getSmsMethod());
			provisioning.setSmsStatusCallback(applicationResponse.getSmsStatusCallback());
			provisioning.setSmsUrl(applicationResponse.getSmsUrl());
			provisioning.setStatusCallBack(applicationResponse.getStatusCallback());
			provisioning.setStatusCallbackMethod(applicationResponse.getStatusCallbackMethod());
			provisioning.setVoiceFallbackMethod(applicationResponse.getVoiceFallbackMethod());
			provisioning.setVoiceFallbackUrl(applicationResponse.getVoiceFallback());
			provisioning.setNumber(number);
			provisioning.setVoiceUrl(applicationResponse.getVoiceUrl() == null ? applicationResponse.getTrunkSid()
					: applicationResponse.getVoiceUrl());
			provisioningDao.updateProvisioning(provisioning);
			purchaseHistory.setNumberProvisioning(provisioning);
			purchaseHistoryDao.createNewPurchaseHistory(purchaseHistory);
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
	}

	public ResourceMaster getResourceMasterByNumberAndProvider(String number, Provider provider) {
		ResourceMaster resourceMaster = null;
		try {
			resourceMaster = resourceMasterDao.getResourceByNumber(number, provider.getId());
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
		return resourceMaster;
	}

	public void deleteResourceMaster(ResourceMaster resourceMaster) {
		try {
			resourceMasterDao.deleteResourceMaster(resourceMaster);
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
	}

	public void deleteResourceAllocation(ResourceMaster resourceMaster) {
		ResourceAllocation resourceAllocation = resourceAllocationDao
				.getResourceAllocationById(resourceMaster.getResourceId());
		try {
			resourceAllocationDao.deleteResourceAllocation(resourceAllocation);
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}

	}

	public void deleteChannelAssetsAllocation(ResourceMaster resourceMaster) {
		try {
			ChannelAssetsAllocation channelAssetsAllocation = channelAssetsAllocationDao
					.getChannelAssetsAllocationByAssetId(resourceMaster.getResourceId());
			channelAssetsAllocationDao.deleteChannelAssetsAllocation(channelAssetsAllocation);
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
	}

	public void updateNewNumbersReleased(ResourceMaster resourceMaster, String clientkey) {
		try {
			NumbersReleased numbersReleased = new NumbersReleased();
			numbersReleased.setChannel(resourceMaster.getChannel());
			numbersReleased.setCountryIso(resourceMaster.getCountryIso());
			numbersReleased.setCreatedOn(ImiDataFormatUtils.fromDateToTimeStamp(resourceMaster.getCreatedOn()));
			numbersReleased.setMonthlyRental(resourceMaster.getMonthlyRentalPrice());
			numbersReleased.setServiceCode(resourceMaster.getServiceCode());
			numbersReleased.setReleasedOn(ImiDataFormatUtils.getCurrentTimeStamp());
			numbersReleased.setUserKey(clientkey);
			numbersReleasedDao.createNewNumbersReleased(numbersReleased);
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
	}

	public void updateCreditsConfigResource(String number, String uuid, String voiceRateCredits)
			throws ClientProtocolException, IOException {
		try {
			String startdate = ImiDataFormatUtils.getCurrentDate("yyyy-MM-dd");
			Date endDate = ImiDataFormatUtils.getDateFromString("9999-12-31", "yyyy-MM-dd");
			long noOfDays = ImiDataFormatUtils.getDifferenceInDaysBetweenDates(new Date(), endDate);
			String resourceRequest = creditsConfigResourceRequest.replace("{number}", number)
					.replace("{teamuuid}", uuid).replace("{startdate}", startdate).replace("{noofdays}", "" + noOfDays)
					.replace("{credits}", voiceRateCredits);
			String resourceUrl = creditsResourceConfigUrl;
			Map<String, String> requestBody = new HashMap<String, String>();
			requestBody.put("authkey", creditsResourceAuthkey);
			requestBody.put("key", uuid);
			requestBody.put("request", resourceRequest);
			LOG.info("Made call to credits config" + "authkey : " + creditsResourceAuthkey + " key : " + uuid
					+ "  request : " + resourceRequest);
			GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(resourceUrl, requestBody,
					ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
			;
			LOG.info("Response from Credits Resource is " + restResponse.getResponseBody() + " with response code "
					+ restResponse.getResponseCode());
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
	}

	public void deletePurchase(ResourceMaster resourceMaster) {
		Purchase purchase = purchaseDao.getPurchaseByNumber(resourceMaster.getServiceCode());
		purchaseDao.deletePurchase(purchase);
	}
}
