package com.imi.rest.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.model.NumberResponse;

@Service
public class PhoneSearchService {

	public List<NumberResponse> searchPhoneNumbers(
			ServiceConstants serviceTypeEnum, String providerId,
			String countryIsoCode, String numberType, String pattern)
			throws ClientProtocolException, IOException {
		List<NumberResponse> phoneSearchResult = null;
		plivioPhoneSearch(serviceTypeEnum, countryIsoCode, numberType, pattern,
				phoneSearchResult);
		twilioPhoneSearch(serviceTypeEnum, countryIsoCode, numberType, pattern,
				phoneSearchResult);
		return phoneSearchResult;
	}

	private void plivioPhoneSearch(ServiceConstants serviceTypeEnum,
			String countryIsoCode, String numberType, String pattern,
			List<NumberResponse> phoneSearchResult)
			throws ClientProtocolException, IOException {
		String plivioPhoneSearchUrl = UrlConstants.PLIVIO_PHONE_SEARCH_URL;
		System.out.println(plivioPhoneSearchUrl);
		System.out.println(serviceTypeEnum);
		plivioPhoneSearchUrl = plivioPhoneSearchUrl
				.replace("{country_iso}", countryIsoCode)
				.replace("{type}", numberType)
				.replace("{services}", serviceTypeEnum.toString())
				.replace("{pattern}", pattern);
		System.out.println(plivioPhoneSearchUrl);
		String authHash = getBasicAuthHash("PLIVIO");
		defaultSearchHandler(plivioPhoneSearchUrl, authHash);

	}

	private void twilioPhoneSearch(ServiceConstants serviceTypeEnum,
			String countryIsoCode, String numberType, String pattern,
			List<NumberResponse> phoneSearchResult)
			throws ClientProtocolException, IOException {
		String twilioPhoneSearchUrl = UrlConstants.TWILIO_PHONE_SEARCH_URL;
		System.out.println(twilioPhoneSearchUrl);
		System.out.println(serviceTypeEnum);
		String servicesString = generateTwilioCapabilities(serviceTypeEnum);
		twilioPhoneSearchUrl = twilioPhoneSearchUrl
				.replace("{country_iso}", countryIsoCode)
				.replace("{services}", servicesString)
				.replace("{pattern}", pattern);
		System.out.println(twilioPhoneSearchUrl);
		String authHash = getBasicAuthHash("TWILIO");
		defaultSearchHandler(twilioPhoneSearchUrl, authHash);

	}
//	private void nexmoPhoneSearch(ServiceConstants serviceTypeEnum,
//			String countryIsoCode, String numberType, String pattern,
//			List<NumberResponse> phoneSearchResult)
//			throws ClientProtocolException, IOException {
//		String nexmoPhoneSearchUrl = UrlConstants.PLIVIO_PHONE_SEARCH_URL;
//		System.out.println(plivioPhoneSearchUrl);
//		System.out.println(serviceTypeEnum);
//		plivioPhoneSearchUrl = plivioPhoneSearchUrl
//				.replace("{country_iso}", countryIsoCode)
//				.replace("{type}", numberType)
//				.replace("{services}", serviceTypeEnum.toString())
//				.replace("{pattern}", pattern);
//		System.out.println(plivioPhoneSearchUrl);
//		String authHash = getBasicAuthHash("PLIVIO");
//		defaultSearchHandler(plivioPhoneSearchUrl, authHash);
//
//	}
	private String defaultSearchHandler(String url, String authHash)
			throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		get.setHeader("Authorization", "Basic " + authHash);
		ResponseHandler<String> handler = new BasicResponseHandler();
		return client.execute(get, handler);
	}

	private String getBasicAuthHash(String provider) {
		String authId = null;
		String authToken = null;
		if (provider.equals("PLIVIO")) {
			authId = "MANMMWNGMWMZNKNDIWOD";
			authToken = "YmM4MWU3MTQxZTk1OTZkMGM2ZmIxYWM1YTBmNWY0";
		}
		if (provider.equals("TWILIO")) {
			authId = "AC606f86ee4172ff7773d4162e7b62496c";
			authToken = "9e2928235fceefb8c92c39a4ceabc0b8";
		}
		String unhashedString = authId + ":" + authToken;
		byte[] authBytes = unhashedString.getBytes(StandardCharsets.UTF_8);
		return DatatypeConverter.printBase64Binary(authBytes);
	}

	private String generateTwilioCapabilities(ServiceConstants serviceTypeEnum) {
		String servicesString = null;
		switch (serviceTypeEnum) {
		case SMS:
			servicesString = "SmsEnabled=true&VoiceEnabled=false";
			break;
		case VOICE:
			servicesString = "SmsEnabled=false&VoiceEnabled=true";
			break;
		case VOICE_SMS:
			servicesString = "SmsEnabled=true&VoiceEnabled=true";
			break;
		default:
			break;
		}

		return servicesString;
	}
}