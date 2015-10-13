package com.imi.rest.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.model.CountryResponse;
import com.imi.rest.model.GenericResponse;
import com.imi.rest.util.BasicAuthUtil;

@Service
public class CountrySearchService {

	public CountryResponse getCountryListWithISO() throws JsonParseException, JsonMappingException, IOException{
		CountryResponse countryResponse = new CountryResponse();
		countryResponse = twilioCountrySearch();
		return countryResponse;
	}
	public CountryResponse  twilioCountrySearch () throws JsonParseException, JsonMappingException, IOException{
		String url = UrlConstants.TWILIO_COUNTRY_LIST_URL;
		String authHash = BasicAuthUtil.getBasicAuthHash(ProviderConstants.TWILIO);
		RestTemplate restTemplate = new RestTemplate();
		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.add("Authorization", "Basic " + authHash);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	    entity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	    String responseBody = entity.getBody();
	    ObjectMapper objMapper = new ObjectMapper();
	    CountryResponse countryResponse  = objMapper.readValue(responseBody, CountryResponse.class);
	    if ( countryResponse != null && countryResponse.getMeta().getNext_page_url() != null ){
	    	String next_page_url = countryResponse.getMeta().getNext_page_url();
	    	HttpEntity<String> entity2 = new HttpEntity<String>("parameters", headers);
	    	entity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		    String responseBody2 = entity.getBody();
		    CountryResponse countryResponse2  = objMapper.readValue(responseBody, CountryResponse.class);
		    countryResponse.addCountries(countryResponse2.getCountries());
	    }
		return countryResponse;
	}
}
