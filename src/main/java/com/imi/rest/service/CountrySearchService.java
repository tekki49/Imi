package com.imi.rest.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.CountryDao;

import com.imi.rest.model.Country;
import com.imi.rest.model.CountryResponse;

@Service
public class CountrySearchService implements ProviderConstants {

	@Autowired
	PlivoFactoryImpl plivioFactoryImpl;

	@Autowired
	TwilioFactoryImpl twilioFactoryImpl;

	@Autowired
	NexmoFactoryImpl nexmoFactoryImpl;

	@Autowired
	CountryDao countryDao;

	@Autowired
	ProviderService providerService;

	private static final Logger LOG = Logger.getLogger(CountrySearchService.class);

	public com.imi.rest.dao.model.Country getCountryById(Integer id) {
		return countryDao.getCountryById(id);
	}

	public com.imi.rest.dao.model.Country getCountryByName(String countryName) {
		return countryDao.getCountryByName(countryName);
	}

	public com.imi.rest.dao.model.Country getCountryByIsoCode(String countryIsoCode) {
		return countryDao.getCountryByIso(countryIsoCode);
	}

	public Set<Country> getCountryListFromDB() {
		return countryDao.getCountrySet();
	}

	public void countryBatchImport() throws JsonParseException, JsonMappingException, IOException {
		Map<String, Map<String, String>> providerCapabilities = new HashMap<String, Map<String, String>>();
		CountryResponse countryResponse = new CountryResponse();
		providerCapabilities.put(TWILIO, new HashMap<String, String>());
		providerCapabilities.put(NEXMO, new HashMap<String, String>());
		providerCapabilities.put(PLIVO, new HashMap<String, String>());
		countryResponse.addCountries(twilioFactoryImpl.importCountries(providerCapabilities));
		countryResponse.addCountries(nexmoFactoryImpl.importCountries(providerCapabilities));
		countryResponse.addCountries(plivioFactoryImpl.importCountries(providerCapabilities));
		Set<Country> countryModelSet = countryResponse.getCountries();
		countryDao.batchUpdate(countryModelSet);
		countryDao.batchUpdate(providerCapabilities.get(TWILIO), providerService.getTwilioProvider());
		countryDao.batchUpdate(providerCapabilities.get(NEXMO), providerService.getNexmoProvider());
		countryDao.batchUpdate(providerCapabilities.get(PLIVO), providerService.getPlivioProvider());
	}

}
