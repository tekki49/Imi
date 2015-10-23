package com.imi.rest.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.core.CountrySearch;
import com.imi.rest.core.NumberSearch;
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryResponse;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.util.BasicAuthUtil;
import com.imi.rest.util.HttpUtil;

@Component
public class TwilioSearchImpl implements NumberSearch, CountrySearch,UrlConstants,ProviderConstants {

    @Override
    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern)
            throws ClientProtocolException, IOException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        String twilioPhoneSearchUrl = TWILIO_PHONE_SEARCH_URL;
        String servicesString = generateTwilioCapabilities(serviceTypeEnum);
        twilioPhoneSearchUrl = twilioPhoneSearchUrl
                .replace("{country_iso}", countryIsoCode)
                .replace("{services}", servicesString)
                .replace("{pattern}", pattern);
        String response = HttpUtil.defaultHttpGetHandler(twilioPhoneSearchUrl,
                BasicAuthUtil.getBasicAuthHash(TWILIO));
        ObjectMapper mapper = new ObjectMapper();
        NumberResponse numberResponse = mapper.readValue(response,
                NumberResponse.class);
        List<Number> twilioNumberList = numberResponse == null ? new ArrayList<Number>()
                : numberResponse.getObjects() == null ? new ArrayList<Number>()
                        : numberResponse.getObjects();
        for (Number twilioNumber : twilioNumberList) {
            if (twilioNumber != null) {
                setServiceType(twilioNumber);
                twilioNumber.setProvider(TWILIO);
                phoneSearchResult.add(twilioNumber);
            }
        }
        return phoneSearchResult;
    }

    @Override
    public void setServiceType(Number number) {
        Map<String, Boolean> capabilties = number.getCapabilities();
        if (capabilties.get("voice") && capabilties.get("SMS")) {
            number.setServiceType(ServiceConstants.BOTH.name());
            number.setSmsEnabled(true);
            number.setVoiceEnabled(true);
        } else if (capabilties.get("SMS")) {
            number.setServiceType(ServiceConstants.SMS.name());
            number.setSmsEnabled(true);
        } else {
            number.setServiceType(ServiceConstants.VOICE.name());
            number.setVoiceEnabled(true);
        }
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
    @Override
	public Set<Country> importCountries() throws JsonParseException,
			JsonMappingException, IOException {
		String url = TWILIO_COUNTRY_LIST_URL;
		String authHash = BasicAuthUtil
				.getBasicAuthHash(ProviderConstants.TWILIO);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + authHash);
		HttpEntity<String> entity = new HttpEntity<String>("parameters",
				headers);
		entity = restTemplate.exchange(url, HttpMethod.GET, entity,
				String.class);
		String responseBody = entity.getBody();
		ObjectMapper objMapper = new ObjectMapper();
		CountryResponse countryResponse = objMapper.readValue(responseBody,
				CountryResponse.class);
		while (countryResponse != null
				&& countryResponse.getMeta().getNextPageUrl() != null) {
			String nextPageUrl = countryResponse.getMeta().getNextPageUrl();
			HttpEntity<String> nextEntity = new HttpEntity<String>(
					"parameters", headers);
			nextEntity = restTemplate.exchange(nextPageUrl, HttpMethod.GET,
					nextEntity, String.class);
			String nextResponseBody = nextEntity.getBody();
			CountryResponse countryResponse2 = objMapper.readValue(
					nextResponseBody, CountryResponse.class);
			countryResponse.addCountries(countryResponse2.getCountries());
		}
		return countryResponse.getCountries();
	}

	public void purchaseNumber(String number, String provider,
			String countryIsoCode) throws ClientProtocolException, IOException {
		String twilioPurchaseUrl = TWILIO_PURCHASE_URL;
		String twilioNumber = "+"+number.trim()+countryIsoCode.trim();
		twilioPurchaseUrl = twilioPurchaseUrl
	                .replace("{number}", twilioNumber);
        ObjectMapper mapper = new ObjectMapper();
		String response = HttpUtil.defaultHttpGetHandler(twilioPurchaseUrl);
	}

	public void releaseNumber(String number, String provider,
			String countryIsoCode) {
		
	}

}
