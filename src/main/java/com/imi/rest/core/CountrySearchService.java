package com.imi.rest.core;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.model.CountryResponse;
import com.imi.rest.util.BasicAuthUtil;

@Service
public class CountrySearchService {

	public CountryResponse getCountryListWithISO() throws JsonParseException, JsonMappingException, IOException{
		CountryResponse countryResponse = twilioCountrySearch();
		return countryResponse;
	}
	public CountryResponse  twilioCountrySearch () throws JsonParseException, JsonMappingException, IOException{
		String url = UrlConstants.TWILIO_COUNTRY_LIST_URL;
		String authHash = BasicAuthUtil.getBasicAuthHash(ProviderConstants.TWILIO);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + authHash);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	    entity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	    String responseBody = entity.getBody();
	    ObjectMapper objMapper = new ObjectMapper();
	    CountryResponse countryResponse  = objMapper.readValue(responseBody, CountryResponse.class);
	    if ( countryResponse != null && countryResponse.getMeta().getNextPageUrl() != null ){
	    	String nextPageUrl = countryResponse.getMeta().getNextPageUrl();
	    	HttpEntity<String> entity2 = new HttpEntity<String>("parameters", headers);
	    	entity2 = restTemplate.exchange(nextPageUrl, HttpMethod.GET, entity, String.class);
		    String responseBody2 = entity2.getBody();
		    CountryResponse countryResponse2  = objMapper.readValue(responseBody2, CountryResponse.class);
		    countryResponse.addCountries(countryResponse2.getCountries());
	    }
		return countryResponse;
	}
}
