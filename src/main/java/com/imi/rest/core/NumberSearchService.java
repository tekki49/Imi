package com.imi.rest.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

	public List<Number> searchPhoneNumbers(
			ServiceConstants serviceTypeEnum, String provider,
			String countryIsoCode, String numberType, String pattern)
			throws ClientProtocolException, IOException {
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

	public List<Number> searchPhoneNumbers(
			ServiceConstants serviceTypeEnum, String countryIsoCode,
			String numberType, String pattern) throws ClientProtocolException,
			IOException {
		List<Number> phoneSearchResult = new ArrayList<Number>();
		plivioPhoneSearch(serviceTypeEnum, countryIsoCode, numberType, pattern,
				phoneSearchResult);
		twilioPhoneSearch(serviceTypeEnum, countryIsoCode, numberType, pattern,
				phoneSearchResult);
		return phoneSearchResult;
	}

	private void plivioPhoneSearch(ServiceConstants serviceTypeEnum,
			String countryIsoCode, String numberType, String pattern,
			List<Number> phoneSearchResult)
			throws ClientProtocolException, IOException {
		String plivioPhoneSearchUrl = UrlConstants.PLIVIO_PHONE_SEARCH_URL;
		plivioPhoneSearchUrl = plivioPhoneSearchUrl
				.replace("{country_iso}", countryIsoCode)
				.replace("{type}", numberType.toLowerCase())
				.replace("{services}", serviceTypeEnum.toString())
				.replace("{pattern}", pattern);
		System.out.println(plivioPhoneSearchUrl);
		ObjectMapper mapper = new ObjectMapper();
		String response=HttpUtil.defaultHttpGetHandler(plivioPhoneSearchUrl,
				BasicAuthUtil.getBasicAuthHash(ProviderConstants.PLIVIO));
		NumberResponse numberResponse=mapper.readValue(response, NumberResponse.class);
		phoneSearchResult.addAll(numberResponse.getObjects());
	}

	private void twilioPhoneSearch(ServiceConstants serviceTypeEnum,
			String countryIsoCode, String numberType, String pattern,
			List<Number> phoneSearchResult)
			throws ClientProtocolException, IOException {
		String twilioPhoneSearchUrl = UrlConstants.TWILIO_PHONE_SEARCH_URL;
		String servicesString = generateTwilioCapabilities(serviceTypeEnum);
		twilioPhoneSearchUrl = twilioPhoneSearchUrl
				.replace("{country_iso}", countryIsoCode)
				.replace("{services}", servicesString)
				.replace("{pattern}", pattern);
		System.out.println("123");
		System.out.println(twilioPhoneSearchUrl);
		String response = HttpUtil.defaultHttpGetHandler(twilioPhoneSearchUrl,
				BasicAuthUtil.getBasicAuthHash(ProviderConstants.TWILIO));
		ObjectMapper mapper = new ObjectMapper();
		NumberResponse numberResponse=mapper.readValue(response, NumberResponse.class);
		phoneSearchResult.addAll(numberResponse.getObjects());
	}

	// private void nexmoPhoneSearch(ServiceConstants serviceTypeEnum,
	// String countryIsoCode, String numberType, String pattern,
	// List<NumberResponse> phoneSearchResult)
	// throws ClientProtocolException, IOException {
	// String nexmoPhoneSearchUrl = UrlConstants.PLIVIO_PHONE_SEARCH_URL;
	// System.out.println(plivioPhoneSearchUrl);
	// System.out.println(serviceTypeEnum);
	// plivioPhoneSearchUrl = plivioPhoneSearchUrl
	// .replace("{country_iso}", countryIsoCode)
	// .replace("{type}", numberType)
	// .replace("{services}", serviceTypeEnum.toString())
	// .replace("{pattern}", pattern);
	// System.out.println(plivioPhoneSearchUrl);
	// String authHash = getBasicAuthHash("PLIVIO");
	// defaultSearchHandler(plivioPhoneSearchUrl, authHash);
	//
	// }

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