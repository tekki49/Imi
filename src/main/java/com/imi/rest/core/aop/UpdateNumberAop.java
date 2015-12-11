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
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.Number;
import com.imi.rest.model.SubAccountDetails;
import com.imi.rest.service.ResourceService;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class UpdateNumberAop implements UrlConstants {

	@Autowired
	ResourceService resourceService;

	@Autowired
	ServiceTypeAop serviceTypeAop;

	private static final Logger LOG = Logger.getLogger(UpdateNumberAop.class);

	public ApplicationResponse updateTwilioNumber(String number, ApplicationResponse applicationResponsetoModify,
			Provider provider, ApplicationResponse incomingPhoneNumber, Integer userid, Integer clientId,
			Integer groupid, Integer teamid, String clientname, String clientkey, SubAccountDetails subAccountDetails)
					throws ClientProtocolException, IOException {
		String twilioNumberUpdateUrl = TWILIO_NUMBER_UPDATE_URL;
		if (incomingPhoneNumber == null || subAccountDetails == null) {
			throw InboundRestException.createApiException(InboundApiErrorCodes.NUMBER_USER_ASSOCIATION_EXCEPTION,
					"Number requested " + number + " does not belong to your " + provider.getName() + "Account");
		}
		twilioNumberUpdateUrl = twilioNumberUpdateUrl.replace("{IncomingPhoneNumberSid}", incomingPhoneNumber.getSid())
				.replace("{auth_id}", subAccountDetails.getSid());
		twilioNumberUpdateUrl = getUpdatedTwilioUrl(twilioNumberUpdateUrl, applicationResponsetoModify);
		ApplicationResponse applicationResponse = new ApplicationResponse();
		HashMap<String, String> requestBody = new HashMap<String, String>();
		updateRequestBodyForTwilio(requestBody, applicationResponsetoModify);
		GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(twilioNumberUpdateUrl, requestBody,
				ImiBasicAuthUtil.getBasicAuthHash(provider), null);
		if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
			applicationResponse = ImiJsonUtil.deserialize(restResponse.getResponseBody(), ApplicationResponse.class);
			resourceService.provisionData(number, applicationResponse);
		} else {
			LOG.error("Some Error occured while updating the number. Please check back again. Response from Twilio is "
					+ restResponse.getResponseBody() + " with response code " + restResponse.getResponseCode());
			throw InboundRestException.createApiException(InboundApiErrorCodes.UNKNOWN_PROVIDER_RESPONSE_EXCEPTION,
					"Some Error occured while updating the number. Please check back again. Response from Twilio is "
							+ restResponse.getResponseBody());
		}
		return applicationResponse;
	}

	private void updateRequestBodyForTwilio(HashMap<String, String> requestBody,
			ApplicationResponse modifyapplication) {
		if (modifyapplication.getFriendlyName() != null) {
			requestBody.put("FriendlyName", modifyapplication.getFriendlyName());
		}
		if (modifyapplication.getApiVersion() != null) {
			requestBody.put("ApiVersion", modifyapplication.getApiVersion());
		}
		if (modifyapplication.getVoiceUrl() != null) {
			requestBody.put("VoiceUrl", modifyapplication.getVoiceUrl());
		}
		if (modifyapplication.getVoiceMethod() != null) {
			requestBody.put("VoiceMethod", modifyapplication.getVoiceMethod());
		}
		if (modifyapplication.getVoiceFallback() != null) {
			requestBody.put("VoiceFallbackUrl", modifyapplication.getVoiceFallback());
		}
		if (modifyapplication.getVoiceFallbackMethod() != null) {
			requestBody.put("VoiceFallbackMethod", modifyapplication.getVoiceFallbackMethod());
		}
		if (modifyapplication.getStatusCallback() != null) {
			requestBody.put("StatusCallback", modifyapplication.getStatusCallback());
		}
		if (modifyapplication.getStatusCallbackMethod() != null) {
			requestBody.put("StatusCallbackMethod", modifyapplication.getStatusCallbackMethod());
		}
		if (modifyapplication.getVoiceCallerIdLookup() != null) {
			requestBody.put("VoiceCallerIdLookup", modifyapplication.getVoiceCallerIdLookup());
		}
		if (modifyapplication.getVoiceApplicationSid() != null) {
			requestBody.put("VoiceApplicationSid", modifyapplication.getVoiceApplicationSid());
		}
		if (modifyapplication.getTrunkSid() != null) {
			requestBody.put("TrunkSid", modifyapplication.getTrunkSid());
		}
		if (modifyapplication.getSmsUrl() != null) {
			requestBody.put("SmsUrl", modifyapplication.getSmsUrl());
		}
		if (modifyapplication.getSmsFallbackUrl() != null) {
			requestBody.put("SmsFallbackUrl", modifyapplication.getSmsFallbackUrl());
		}
		if (modifyapplication.getSmsFallbackMethod() != null) {
			requestBody.put("SmsFallbackMethod", modifyapplication.getSmsFallbackMethod());
		}
		if (modifyapplication.getSmsApplicationSid() != null) {
			requestBody.put("SmsApplicationSid", modifyapplication.getSmsApplicationSid());
		}
		if (modifyapplication.getAccountSid() != null) {
			requestBody.put("AccountSid", modifyapplication.getAccountSid());
		}
	}

	private String getUpdatedTwilioUrl(String url, ApplicationResponse modifyapplication) {
		String toAppend = "?";
		if (modifyapplication.getFriendlyName() != null) {
			toAppend = toAppend.concat("FriendlyName=" + modifyapplication.getFriendlyName() + "&");
		}
		if (modifyapplication.getApiVersion() != null) {
			toAppend = toAppend.concat("ApiVersion=" + modifyapplication.getApiVersion() + "&");
		}
		if (modifyapplication.getVoiceUrl() != null) {
			toAppend = toAppend.concat("VoiceUrl=" + modifyapplication.getVoiceUrl() + "&");
		}
		if (modifyapplication.getVoiceMethod() != null) {
			toAppend = toAppend.concat("VoiceMethod=" + modifyapplication.getVoiceMethod() + "&");
		}
		if (modifyapplication.getVoiceFallback() != null) {
			toAppend = toAppend.concat("VoiceFallbackUrl=" + modifyapplication.getVoiceFallback() + "&");
		}
		if (modifyapplication.getVoiceFallbackMethod() != null) {
			toAppend = toAppend.concat("VoiceFallbackMethod=" + modifyapplication.getVoiceFallbackMethod() + "&");
		}
		if (modifyapplication.getStatusCallback() != null) {
			toAppend = toAppend.concat("StatusCallback=" + modifyapplication.getStatusCallback() + "&");
		}
		if (modifyapplication.getStatusCallbackMethod() != null) {
			toAppend = toAppend.concat("StatusCallbackMethod=" + modifyapplication.getStatusCallbackMethod() + "&");
		}
		if (modifyapplication.getVoiceCallerIdLookup() != null) {
			toAppend.concat("VoiceCallerIdLookup=" + modifyapplication.getVoiceCallerIdLookup() + "&");
		}
		if (modifyapplication.getVoiceApplicationSid() != null) {
			toAppend = toAppend.concat("VoiceApplicationSid=" + modifyapplication.getVoiceApplicationSid() + "&");
		}
		if (modifyapplication.getTrunkSid() != null) {
			toAppend = toAppend.concat("TrunkSid=" + modifyapplication.getTrunkSid() + "&");
		}
		if (modifyapplication.getSmsUrl() != null) {
			toAppend = toAppend.concat("SmsUrl=" + modifyapplication.getSmsUrl() + "&");
		}
		if (modifyapplication.getSmsFallbackUrl() != null) {
			toAppend = toAppend.concat("SmsFallbackUrl=" + modifyapplication.getSmsFallbackUrl() + "&");
		}
		if (modifyapplication.getSmsFallbackMethod() != null) {
			toAppend = toAppend.concat("SmsFallbackMethod=" + modifyapplication.getSmsFallbackMethod() + "&");
		}
		if (modifyapplication.getSmsApplicationSid() != null) {
			toAppend = toAppend.concat("SmsApplicationSid=" + modifyapplication.getSmsApplicationSid() + "&");
		}
		if (modifyapplication.getAccountSid() != null) {
			toAppend = toAppend.concat("AccountSid=" + modifyapplication.getAccountSid() + "&");
		}
		toAppend = toAppend.substring(0, toAppend.length() - 1);
		url = url.concat(toAppend);
		return url;
	}

	public ApplicationResponse updateNexmoNumber(String number, String countryIsoCode, ApplicationResponse application,
			Provider provider, Number numberDetails, Integer userid, Integer clientId, Integer groupid, Integer teamid,
			String clientname, String clientkey) throws ClientProtocolException, IOException {
		String nexmoNumber = (number.trim().replace("+", ""));
		if (numberDetails == null) {
			throw InboundRestException.createApiException(InboundApiErrorCodes.NUMBER_USER_ASSOCIATION_EXCEPTION,
					"Number requested " + number + " does not belong to your " + provider.getName() + "Account");
		}
		if (countryIsoCode.equals("")) {
			throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_COUNTRY_CODE_EXCEPTION,
					"CountryIsoCode is required for provisioning Nexmo number. Please use url /number/update/{countryIsoCode}/{number} ");
		}
		serviceTypeAop.setNexmoServiceType(numberDetails);
		String nexmoAccountUpdateUrl = NEXMO_NUMBER_UPDATE_URL;
		nexmoAccountUpdateUrl = nexmoAccountUpdateUrl.replace("{country}", countryIsoCode)
				.replace("{api_key}", provider.getAuthId()).replace("{api_secret}", provider.getApiKey())
				.replace("{msisdn}", nexmoNumber);
		nexmoAccountUpdateUrl = getUpdatedNexmoUrl(nexmoAccountUpdateUrl, application);
		GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(nexmoAccountUpdateUrl,
				ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
		ApplicationResponse applicationResponse = new ApplicationResponse();
		if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
			applicationResponse.setMoHttpUrl(application.getMoHttpUrl());
			applicationResponse.setPhone_number(nexmoNumber);
			applicationResponse.setMoSmppSysType(application.getMoSmppSysType());
			applicationResponse.setSmsUrl(application.getMoHttpUrl());
			applicationResponse.setVoiceUrl(application.getVoiceCallbackValue());
			applicationResponse.setVoiceCallbackType(application.getVoiceCallbackType());
			applicationResponse.setVoiceCallbackValue(application.getVoiceCallbackValue());
			applicationResponse.setStatusCallback(application.getStatusCallback());
			resourceService.provisionData(nexmoNumber, applicationResponse);
		} else if (restResponse.getResponseCode() == HttpStatus.METHOD_FAILURE.value()) {
			String message = "Number was not updated successfully, " + "Some Parameters to update Number were wrong "
					+ "Please Check Whether appropriate values are being sent";
			LOG.error("Some Error occured while updating the number. Please check back again. Response from Twilio is "
					+ restResponse.getResponseBody() + " with response code " + restResponse.getResponseCode());
			throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_API_PARAMETERS_EXCEPTION,
					message);
		} else if (restResponse.getResponseCode() == HttpStatus.UNAUTHORIZED.value()) {
			LOG.error("Some Error occured while updating the number. Please check back again. Response from Twilio is "
					+ restResponse.getResponseBody() + " with response code " + restResponse.getResponseCode());
			throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION,
					"Invalid provider details used while updating the number Response from Nexmo "
							+ restResponse.getResponseBody());
		} else {
			LOG.error("Some Error occured while updating the number. Please check back again. Response from Twilio is "
					+ restResponse.getResponseBody() + " with response code " + restResponse.getResponseCode());
			throw InboundRestException.createApiException(InboundApiErrorCodes.UNKNOWN_PROVIDER_RESPONSE_EXCEPTION,
					"Response from Nexmo is " + restResponse.getResponseBody() + "with response code "
							+ restResponse.getResponseCode());
		}
		return applicationResponse;
	}

	private String getUpdatedNexmoUrl(String url, ApplicationResponse modifyapplication) {
		String toAppend = "?";
		if (modifyapplication.getMoHttpUrl() != null) {
			toAppend = toAppend.concat("moHttpUrl=" + modifyapplication.getMoHttpUrl() + "&");
		}
		if (modifyapplication.getMoSmppSysType() != null) {
			toAppend = toAppend.concat("moSmppSysType=" + modifyapplication.getMoSmppSysType() + "&");
		}
		if (modifyapplication.getVoiceCallbackType() != null) {
			toAppend = toAppend.concat("voiceCallbackType=" + modifyapplication.getVoiceCallbackType() + "&");
		}
		if (modifyapplication.getVoiceCallbackValue() != null) {
			toAppend = toAppend.concat("voiceCallbackValue=" + modifyapplication.getVoiceCallbackValue() + "&");
		}
		if (modifyapplication.getStatusCallback() != null) {
			toAppend = toAppend.concat("voiceStatusCallback=" + modifyapplication.getStatusCallback() + "&");
		}
		toAppend = toAppend.substring(0, toAppend.length() - 1);
		url = url.concat(toAppend);
		return url;
	}
}
