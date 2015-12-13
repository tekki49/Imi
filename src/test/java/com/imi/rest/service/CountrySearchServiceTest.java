package com.imi.rest.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.CountryDao;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryResponse;

public class CountrySearchServiceTest {

	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	PlivoFactoryImpl plivoFactoryImpl;
	@Mock
	NexmoFactoryImpl nexmoFactoryImpl;
	@Mock
	CountryDao countryDao;
	@Mock
	ProviderService providerService;
	@InjectMocks
	CountrySearchService countrySearchService;

	private Map<String, Map<String, String>> providerCapabilities;
	private Set<com.imi.rest.model.Country> countrySet;
	CountryResponse countryResponse;
	Country country;

	@Before
	public void setUp() {
		providerCapabilities = new HashMap<String, Map<String, String>>();
		countryDao = new CountryDao();
		providerService = new ProviderService();
		countryResponse = new CountryResponse();
		countryResponse.addCountries(countrySet);
		providerCapabilities.put(com.imi.rest.constants.ProviderConstants.TWILIO, new HashMap<String, String>());
		providerCapabilities.put(com.imi.rest.constants.ProviderConstants.NEXMO, new HashMap<String, String>());
		providerCapabilities.put(com.imi.rest.constants.ProviderConstants.PLIVO, new HashMap<String, String>());
		countrySet = new TreeSet<com.imi.rest.model.Country>();
		country = new Country();
		country.setCountry("United States");
		country.setCountryCode("01");
		country.setIsoCountry("US");
		countrySet.add(country);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void countryBatchImport() throws JsonParseException, JsonMappingException, IOException {
		doReturn(countrySet).when(twilioFactoryImpl).importCountries(providerCapabilities);
		doReturn(countrySet).when(plivoFactoryImpl).importCountries(providerCapabilities);
		doReturn(countrySet).when(nexmoFactoryImpl).importCountries(providerCapabilities);
		countryResponse = Mockito.mock(CountryResponse.class);
		doNothing().when(countryResponse).addCountries(countrySet);
		countryResponse.addCountries(countrySet);
		Set<Country> countryModelSet = countryResponse.getCountries();
		countryDao.batchUpdate(countryModelSet);
		Provider providerTwilio = new Provider();
		providerTwilio.setName("TWILIO");
		Provider providerPlivo = new Provider();
		providerPlivo.setName("PLIVO");
		Provider providerNexmo = new Provider();
		providerNexmo.setName("NEXMO");
		providerService = Mockito.mock(ProviderService.class);
		doReturn(providerTwilio).when(providerService).getTwilioProvider();
		doReturn(providerPlivo).when(providerService).getTwilioProvider();
		doReturn(providerNexmo).when(providerService).getTwilioProvider();
		countryDao = Mockito.mock(CountryDao.class);
		doNothing().when(countryDao)
				.batchUpdate(providerCapabilities.get(com.imi.rest.constants.ProviderConstants.TWILIO), providerTwilio);
		doNothing().when(countryDao)
				.batchUpdate(providerCapabilities.get(com.imi.rest.constants.ProviderConstants.NEXMO), providerNexmo);
		doNothing().when(countryDao)
				.batchUpdate(providerCapabilities.get(com.imi.rest.constants.ProviderConstants.PLIVO), providerPlivo);
	}
}
