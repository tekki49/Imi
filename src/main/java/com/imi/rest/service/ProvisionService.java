package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.VoiceRouteMasterDao;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.VoiceRouteMaster;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.ApplicationResponse;

@Service
public class ProvisionService implements ProviderConstants {
	@Autowired
	TwilioFactoryImpl twilioFactoryImpl;
	@Autowired
	PlivoFactoryImpl plivoFactoryImpl;
	@Autowired
	NexmoFactoryImpl nexmoFactoryImpl;
	@Autowired
	ProviderService providerService;
	@Autowired
	CountrySearchService countrySearchService;
	@Autowired
	VoiceRouteMasterDao voiceRouteMasterDao;

	public ApplicationResponse provisionNumber(String number, String countryIsoCode, Provider provider, Integer userid,
			Integer clientId, Integer groupid, Integer teamid, String clientname, String clientkey)
					throws ClientProtocolException, IOException {
		Country country = countrySearchService.getCountryByIsoCode(countryIsoCode);
		ApplicationResponse applicationResponse = new ApplicationResponse();
		VoiceRouteMaster voiceRouteMaster = voiceRouteMasterDao.getResourceById(provider.getId(),
				country.getCountryCode(), country.getCountryIso());
		ApplicationResponse application = new ApplicationResponse();
		if (provider.getName().equalsIgnoreCase(PLIVO)) {
			application.setApp_id(voiceRouteMaster.getDvpCallbackUrl());
			applicationResponse = plivoFactoryImpl.updateNumber(number, application,
					providerService.getPlivioProvider(), userid, clientId, groupid, teamid, clientname, clientkey);
		} else if (provider.getName().equalsIgnoreCase(TWILIO)) {
			application.setSmsUrl(provider.getSms_callback_url());
			// TODO get the subaccount trunksid and set it instead from voice
			// route master
			application.setTrunkSid(voiceRouteMaster.getDvpCallbackUrl());
			applicationResponse = twilioFactoryImpl.updateNumber(number, application,
					providerService.getTwilioProvider(), userid, clientId, groupid, teamid, clientname, clientkey);
		} else if (provider.getName().equalsIgnoreCase(NEXMO)) {
			number = number.replace("+", "");
			application.setMoHttpUrl(provider.getSms_callback_url());
			application.setVoiceCallbackValue(number + "@" + voiceRouteMaster.getDvpCallbackUrl());
			application.setVoiceCallbackType("sip");
			applicationResponse = nexmoFactoryImpl.updateNumber(number, countryIsoCode, application,
					providerService.getNexmoProvider(), userid, clientId, groupid, teamid, clientname, clientkey);
		} else {
			String message = "Provider " + provider.getName() + " is invalid";
			throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION, message);
		}
		return applicationResponse;
	}

	public ApplicationResponse provisionAllNumbers(String providerName, ApplicationResponse application, Integer userid,
			Integer clientId, Integer groupid, Integer teamid, String clientname, String clientkey)
					throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		if (providerName.equalsIgnoreCase(PLIVO)) {
			applicationResponse = plivoFactoryImpl.updateAllNumbers(application, providerService.getPlivioProvider(),
					userid, clientId, groupid, teamid, clientname, clientkey);
		} else {
			String message = "Provisioning all numbers is not supported by Provider " + providerName;
			throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_PROVIDER_ACTION_EXCEPTION,
					message);
		}
		return applicationResponse;
	}

	public ApplicationResponse updateApplication(String providerName, ApplicationResponse application)
			throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		if (providerName.equalsIgnoreCase(PLIVO)) {
			applicationResponse = plivoFactoryImpl.updateApplication(application, providerService.getPlivioProvider());
		} else {
			String message = "Application cannot be created for Provider " + providerName;
			throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_PROVIDER_ACTION_EXCEPTION,
					message);
		}
		return applicationResponse;
	}

	public ApplicationResponse createApplication(String providerName, ApplicationResponse application)
			throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		if (providerName.equalsIgnoreCase(PLIVO)) {
			applicationResponse = plivoFactoryImpl.createApplication(application, providerService.getPlivioProvider());
		} else {
			String message = "Application cannot be created for Provider " + providerName;
			throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_PROVIDER_ACTION_EXCEPTION,
					message);
		}
		return applicationResponse;
	}

	public ApplicationResponse getApplication(String providerName, String app_id)
			throws ClientProtocolException, IOException {
		ApplicationResponse applicationResponse = new ApplicationResponse();
		if (providerName.equalsIgnoreCase(PLIVO)) {
			applicationResponse = plivoFactoryImpl.getApplication(app_id, providerService.getPlivioProvider());
		} else {
			String message = "Application cannot be created for Provider " + providerName;
			throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_PROVIDER_ACTION_EXCEPTION,
					message);
		}
		return applicationResponse;
	}
}
