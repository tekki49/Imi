package com.imi.rest.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryResponse;
import com.imi.rest.util.BasicAuthUtil;

@Service
public class CountrySearchService {
	String PLIVIO_CSV_FILE_PATH = "/home/hemanth/Desktop/PLIVIO_COUNTRIES_WITH_ISO.csv";
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
	    while (countryResponse!=null && countryResponse.getMeta().getNextPageUrl() != null ) {
	    	String nextPageUrl = countryResponse.getMeta().getNextPageUrl();
	    	HttpEntity<String> nextEntity = new HttpEntity<String>("parameters", headers);
	    	nextEntity = restTemplate.exchange(nextPageUrl, HttpMethod.GET, nextEntity, String.class);
		    String NextResponseBody = nextEntity.getBody();
		    CountryResponse countryResponse2  = objMapper.readValue(NextResponseBody, CountryResponse.class);
		    countryResponse.addCountries(countryResponse2.getCountries());
		}
	    Set<Country>  plivioCountriesSet =  importPlivioCountriesFromCSV();
	    countryResponse.addCountries(plivioCountriesSet);
		return countryResponse;
	}
	public Set<Country> importPlivioCountriesFromCSV() throws FileNotFoundException{
		Set<Country> countriesSet = new HashSet<Country>();
		String line = "";
		String splitBy = ",";
		BufferedReader reader = null;
		try {
			FileReader fileReader = new FileReader(PLIVIO_CSV_FILE_PATH);
			reader = new BufferedReader(fileReader);
			while ( (line=reader.readLine()) != null) {
				Country country = new Country();
				country.setCountry(line.split(splitBy)[0]);
				country.setIso_country(line.split(splitBy)[1]);
				countriesSet.add(country);
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}finally{
			if( reader != null){
				try {
					reader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		return countriesSet;
	}
}
