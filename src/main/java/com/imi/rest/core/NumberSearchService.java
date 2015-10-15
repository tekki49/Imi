package com.imi.rest.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.util.BasicAuthUtil;
import com.imi.rest.util.HttpUtil;

@Service
public class NumberSearchService {

	public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
			String provider, String countryIsoCode, String numberType,
			String pattern) throws ClientProtocolException, IOException {
		List<Number> phoneSearchResult = new ArrayList<Number>();
		if (provider.equalsIgnoreCase(ProviderConstants.PLIVIO)) {
			plivioPhoneSearch(serviceTypeEnum, countryIsoCode, numberType,
					pattern, phoneSearchResult);
		} else if (provider.equalsIgnoreCase(ProviderConstants.TWILIO)) {
			twilioPhoneSearch(serviceTypeEnum, countryIsoCode, numberType,
					pattern, phoneSearchResult);
		}
		return phoneSearchResult;
	}

	public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
			String countryIsoCode, String numberType, String pattern)
			throws ClientProtocolException, IOException {
		List<Number> phoneSearchResult = new ArrayList<Number>();
		plivioPhoneSearch(serviceTypeEnum, countryIsoCode, numberType, pattern,
				phoneSearchResult);
		twilioPhoneSearch(serviceTypeEnum, countryIsoCode, numberType, pattern,
				phoneSearchResult);
		nexmoPhoneSearch(serviceTypeEnum, countryIsoCode, numberType, pattern,
				phoneSearchResult, Integer.MIN_VALUE, 1);
		return phoneSearchResult;
	}

	private void plivioPhoneSearch(ServiceConstants serviceTypeEnum,
			String countryIsoCode, String numberType, String pattern,
			List<Number> phoneSearchResult) throws ClientProtocolException,
			IOException {
		String plivioPhoneSearchUrl = UrlConstants.PLIVIO_PHONE_SEARCH_URL;
		plivioPhoneSearchUrl = plivioPhoneSearchUrl
				.replace("{country_iso}", countryIsoCode)
				.replace("{type}", numberType.toLowerCase())
				.replace("{services}", serviceTypeEnum.toString())
				.replace("{pattern}", pattern);
		ObjectMapper mapper = new ObjectMapper();
		String response = HttpUtil.defaultHttpGetHandler(plivioPhoneSearchUrl,
				BasicAuthUtil.getBasicAuthHash(ProviderConstants.PLIVIO));
		NumberResponse numberResponse = mapper.readValue(response,
				NumberResponse.class);
		List<Number> plivioNumberList = numberResponse == null ? new ArrayList<Number>()
				: numberResponse.getObjects() == null ? new ArrayList<Number>()
						: numberResponse.getObjects();
		for (Number plivioNumber : plivioNumberList) {
			if (plivioNumber != null) {
				plivioNumber.setProvider(ProviderConstants.PLIVIO);
				setServiceType(plivioNumber, ProviderConstants.PLIVIO);
				phoneSearchResult.add(plivioNumber);
			}
		}
	}

	private void setServiceType(Number number, String provider) {
		if (provider.equals(ProviderConstants.PLIVIO)) {
			if (number.isSmsEnabled() && number.isVoiceEnabled()) {
				number.setServiceType(ServiceConstants.BOTH.name());
			} else if (number.isSmsEnabled()) {
				number.setServiceType(ServiceConstants.SMS.name());
			} else {
				number.setServiceType(ServiceConstants.VOICE.name());
			}
		} else if (provider.equals(ProviderConstants.TWILIO)) {
			Map<String, Boolean> capabilties = number.getCapabilities();
			if (capabilties.get("voice") && capabilties.get("SMS")) {
				number.setServiceType(ServiceConstants.BOTH.name());
			} else if (capabilties.get("SMS")) {
				number.setServiceType(ServiceConstants.SMS.name());
			} else {
				number.setServiceType(ServiceConstants.VOICE.name());
			}
		} else if (provider.equals(ProviderConstants.NEXMO)) {
			List<String> features = number.getFeatures();
			if (features.contains(ServiceConstants.SMS.name())
					&& features.contains(ServiceConstants.VOICE.name())) {
				number.setServiceType(ServiceConstants.BOTH.name());
				number.setSmsEnabled(true);
				number.setVoiceEnabled(true);
			} else if (features.contains(ServiceConstants.SMS.name())) {
				number.setServiceType(ServiceConstants.SMS.name());
				number.setSmsEnabled(true);
			} else {
				number.setServiceType(ServiceConstants.VOICE.name());
				number.setVoiceEnabled(true);
			}
		}
	}

	private void twilioPhoneSearch(ServiceConstants serviceTypeEnum,
			String countryIsoCode, String numberType, String pattern,
			List<Number> phoneSearchResult) throws ClientProtocolException,
			IOException {
		String twilioPhoneSearchUrl = UrlConstants.TWILIO_PHONE_SEARCH_URL;
		String servicesString = generateTwilioCapabilities(serviceTypeEnum);
		twilioPhoneSearchUrl = twilioPhoneSearchUrl
				.replace("{country_iso}", countryIsoCode)
				.replace("{services}", servicesString)
				.replace("{pattern}", pattern);
		String response = HttpUtil.defaultHttpGetHandler(twilioPhoneSearchUrl,
				BasicAuthUtil.getBasicAuthHash(ProviderConstants.TWILIO));
		ObjectMapper mapper = new ObjectMapper();
		NumberResponse numberResponse = mapper.readValue(response,
				NumberResponse.class);
		List<Number> twilioNumberList = numberResponse == null ? new ArrayList<Number>()
				: numberResponse.getObjects() == null ? new ArrayList<Number>()
						: numberResponse.getObjects();
		for (Number twilioNumber : twilioNumberList) {
			if (twilioNumber != null) {
				setServiceType(twilioNumber, ProviderConstants.TWILIO);
				twilioNumber.setProvider(ProviderConstants.TWILIO);
				phoneSearchResult.add(twilioNumber);
			}
		}
		phoneSearchResult.addAll(numberResponse.getObjects());
	}

	private void nexmoPhoneSearch(ServiceConstants serviceTypeEnum,
			String countryIsoCode, String numberType, String pattern,
			List<Number> phoneSearchResult, int count, int index)
			throws ClientProtocolException, IOException {
		String nexmoPhoneSearchUrl = UrlConstants.NEXMO_PHONE_SEARCH_URL;
		if (Integer.MIN_VALUE == count) {
			index = 1;
		} else if (count - (index * 100) > 0) {
			index++;
		} else {
			index = -1;
			return;
		}
		nexmoPhoneSearchUrl = nexmoPhoneSearchUrl
				.replace("{country_iso}", countryIsoCode)
				.replace("{api_key}", "a5eb8aa1")
				.replace("{api_secret}", "b457a519")
				.replace("{pattern}", pattern)
				.replace("{features}", serviceTypeEnum.toString().toUpperCase())
				.replace("{index}", "" + index);
		String response = HttpUtil.defaultHttpGetHandler(nexmoPhoneSearchUrl);
		ObjectMapper mapper = new ObjectMapper();
		NumberResponse numberResponse = mapper.readValue(response,
				NumberResponse.class);
		if (numberResponse == null)
			return;
		List<Number> nexmoNumberList = numberResponse == null ? new ArrayList<Number>()
				: numberResponse.getObjects() == null ? new ArrayList<Number>()
						: numberResponse.getObjects();
		for (Number nexmoNumber : nexmoNumberList) {
			if (nexmoNumber != null) {
				setServiceType(nexmoNumber, ProviderConstants.NEXMO);
				nexmoNumber.setProvider(ProviderConstants.NEXMO);
				phoneSearchResult.add(nexmoNumber);
			}
		}
		count = numberResponse.getCount();
		phoneSearchResult.addAll(numberResponse.getObjects());
		nexmoPhoneSearch(serviceTypeEnum, countryIsoCode, numberType, pattern,
				phoneSearchResult, count, index);
	}

	private String generateTwilioCapabilities(ServiceConstants serviceTypeEnum) {
		String servicesString = null;
		switch (serviceTypeEnum) {
		case SMS:
			servicesString = "SmsEnabled=true";
			break;
		case VOICE:
			servicesString = "VoiceEnabled=true";
			break;
		case BOTH:
			servicesString = "SmsEnabled=true&VoiceEnabled=true";
			break;
		default:
			break;
		}
		return servicesString;
	}
}