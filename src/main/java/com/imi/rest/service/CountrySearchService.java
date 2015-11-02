package com.imi.rest.service;

import java.io.IOException;
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
import com.imi.rest.exception.ImiException;
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

    private static final Logger LOG = Logger
            .getLogger(CountrySearchService.class);

    public Set<Country> getCountryListWithISO() throws JsonParseException,
            JsonMappingException, IOException, ImiException {
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.addCountries(twilioFactoryImpl
                .importCountriesByUrl(providerService.getTwilioProvider()));
        countryResponse.addCountries(nexmoFactoryImpl.importCountries());
        countryResponse.addCountries(plivioFactoryImpl.importCountries());
        return countryResponse.getCountries();
    }

    public com.imi.rest.dao.model.Country getCountryById(Integer id) {
        return countryDao.getCountryById(id);
    }

    public com.imi.rest.dao.model.Country getCountryByName(String countryName) {
        return countryDao.getCountryByName(countryName);
    }

    public com.imi.rest.dao.model.Country getCountryByIsoCode(
            String countryIsoCode) {
        return countryDao.getCountryByIso(countryIsoCode);
    }

    public Set<Country> getCountryListFromDB() throws ImiException {
        return countryDao.getCountrySet();
    }

    public void countryBatchImport() throws JsonParseException,
            JsonMappingException, IOException, ImiException {
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.addCountries(twilioFactoryImpl.importCountries());
        countryResponse.addCountries(nexmoFactoryImpl.importCountries());
        countryResponse.addCountries(plivioFactoryImpl.importCountries());
        Set<Country> countryModelSet = countryResponse.getCountries();
        countryDao.batchUpdate(countryModelSet);
    }

}
