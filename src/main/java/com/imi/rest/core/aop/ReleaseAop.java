package com.imi.rest.core.aop;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.imi.rest.constants.UrlConstants;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.ResourceMaster;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.Number;
import com.imi.rest.model.SubAccountDetails;
import com.imi.rest.service.ResourceService;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiHttpUtil;

@Component
public class ReleaseAop implements UrlConstants {

	@Autowired
	ResourceService resourceService;

	private static final Logger LOG = Logger.getLogger(ReleaseAop.class);

	public void releaseTwilioNumber(String number, Provider provider, String countryIsoCode,
			ApplicationResponse incomingPhoneNumber, SubAccountDetails subAccountDetails, String clientkey)
					throws IOException {
		if (incomingPhoneNumber == null || subAccountDetails == null) {
			LOG.error("Number requested " + number + " does not belong to your " + provider.getName() + "Account");
			throw InboundRestException.createApiException(InboundApiErrorCodes.NUMBER_USER_ASSOCIATION_EXCEPTION,
					"Number requested " + number + " does not belong to your " + provider.getName() + "Account");
		}
		String incomingPhoneNumberSid = incomingPhoneNumber.getSid();
		String twilioReleaseurl = TWILIO_RELEASE_URL;
		twilioReleaseurl = twilioReleaseurl.replace("{auth_id}", subAccountDetails.getSid())
				.replace("{IncomingPhoneNumberSid}", incomingPhoneNumberSid);
		GenericRestResponse response = ImiHttpUtil.defaultHttpDeleteHandler(twilioReleaseurl,
				new HashMap<String, String>(), ImiBasicAuthUtil.getBasicAuthHash(provider), null);
		if (response.getResponseCode() != HttpStatus.NO_CONTENT.value()) {
			throw InboundRestException.createApiException(InboundApiErrorCodes.NUMBER_PROVIDER_ASSOCIATION_EXCEPTION,
					"Number requested " + number + " does not belong to your " + provider.getName() + "Account");
		} else {
			ResourceMaster resourceMaster = resourceService.getResourceMasterByNumberAndProvider(number, provider);
			resourceService.deleteChannelAssetsAllocation(resourceMaster);
			resourceService.deleteResourceAllocation(resourceMaster);
			resourceService.updateNewNumbersReleased(resourceMaster, clientkey);
			resourceService.deletePurchase(resourceMaster);
			resourceService.deleteResourceMaster(resourceMaster);
		}
	}

	public void releaseNexmoNumber(String number, Provider provider, String countryIsoCode, Number numberDetails,
			String clientkey) throws ClientProtocolException, IOException {
		String nexmoReleaseUrl = NEXMO_RELEASE_URL;
		String nexmoNumber = (number.trim().replace("+", ""));
		if (numberDetails == null) {
			throw InboundRestException.createApiException(InboundApiErrorCodes.NUMBER_PROVIDER_ASSOCIATION_EXCEPTION,
					"Number requested " + number + " does not belong to your " + provider.getName() + "Account");
		}
		nexmoReleaseUrl = nexmoReleaseUrl.replace("{api_key}", provider.getAuthId())
				.replace("{api_secret}", provider.getApiKey()).replace("{country}", countryIsoCode)
				.replace("{msisdn}", nexmoNumber);
		GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(nexmoReleaseUrl,
				new HashMap<String, String>(), ImiBasicAuthUtil.getBasicAuthHash(provider),
				ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
		if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
			ResourceMaster resourceMaster = resourceService.getResourceMasterByNumberAndProvider(number, provider);
			resourceService.deleteChannelAssetsAllocation(resourceMaster);
			resourceService.deleteResourceAllocation(resourceMaster);
			resourceService.updateNewNumbersReleased(resourceMaster, clientkey);
			resourceService.deleteResourceMaster(resourceMaster);
		} else if (restResponse.getResponseCode() == HttpStatus.UNAUTHORIZED.value()) {
			LOG.error("Invalid provider authentication details for " + provider.getName()
					+ " response from service provider is " + restResponse.getResponseBody() + " response code is "
					+ restResponse.getResponseCode());
			throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION,
					"Nexmo Provider details while accessing Release api are invalid.Response from Nexmo is"
							+ restResponse.getResponseBody());

		} else if (restResponse.getResponseCode() == HttpStatus.METHOD_FAILURE.value()) {
			LOG.error("Invalid details while releasing the number " + number + " for " + provider.getName()
					+ " response from service provider is " + restResponse.getResponseBody() + " response code is "
					+ restResponse.getResponseCode());
			throw InboundRestException.createApiException(InboundApiErrorCodes.NUMBER_PROVIDER_ASSOCIATION_EXCEPTION,
					"Number " + number + " does not belong to Nexmo master account.Response from Nexmo is"
							+ restResponse.getResponseBody());
		} else {
			LOG.error("Invalid details while releasing the number " + number + " for " + provider.getName()
					+ " response from service provider is " + restResponse.getResponseBody() + " response code is "
					+ restResponse.getResponseCode());
			throw InboundRestException.createApiException(InboundApiErrorCodes.UNKNOWN_PROVIDER_RESPONSE_EXCEPTION,
					"Response from Nexmo " + restResponse.getResponseBody());
		}
	}

	public void releasePlivoNumber(String number, Provider provider, String countryIsoCode, Number numberDetails,
			String clientkey) throws ClientProtocolException, IOException {
		String plivoNumber = (number.trim().replace("+", ""));
		if (numberDetails == null) {
			throw InboundRestException.createApiException(InboundApiErrorCodes.NUMBER_PROVIDER_ASSOCIATION_EXCEPTION,
					"Number requested " + number + " does not belong to your " + provider.getName() + "Account");
		}
		String plivioReleaseurl = PLIVO_RELEASE_URL;
		plivioReleaseurl = plivioReleaseurl.replace("{number}", plivoNumber).replace("{auth_id}", provider.getAuthId());
		GenericRestResponse restResponse = ImiHttpUtil.defaultHttpDeleteHandler(plivioReleaseurl,
				new HashMap<String, String>(), ImiBasicAuthUtil.getBasicAuthHash(provider), null);
		if (restResponse.getResponseCode() != HttpStatus.NO_CONTENT.value()) {
			LOG.error("Invalid details while releasing the number " + number + " for " + provider.getName()
					+ " response from service provider is " + restResponse.getResponseBody() + " response code is "
					+ restResponse.getResponseCode());
			throw InboundRestException.createApiException(InboundApiErrorCodes.UNKNOWN_PROVIDER_RESPONSE_EXCEPTION,
					"Response from Plivo" + restResponse.getResponseBody());
		} else {
			ResourceMaster resourceMaster = resourceService.getResourceMasterByNumberAndProvider(number, provider);
			resourceService.deleteChannelAssetsAllocation(resourceMaster);
			resourceService.deleteResourceAllocation(resourceMaster);
			resourceService.updateNewNumbersReleased(resourceMaster, clientkey);
			resourceService.deleteResourceMaster(resourceMaster);
		}
	}

}
