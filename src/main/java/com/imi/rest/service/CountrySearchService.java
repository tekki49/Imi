package com.imi.rest.service;

import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivioFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryResponse;

@Service
public class CountrySearchService {

    @Autowired
    PlivioFactoryImpl plivioFactoryImpl;

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;

    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;

    private static final Logger LOG = Logger
            .getLogger(CountrySearchService.class);

    public Set<Country> getCountryListWithISO()
            throws JsonParseException, JsonMappingException, IOException {
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.addCountries(twilioFactoryImpl.importCountries());
        countryResponse.addCountries(nexmoFactoryImpl.importCountries());
        countryResponse.addCountries(plivioFactoryImpl.importCountries());
        return countryResponse.getCountries();
    }

}
