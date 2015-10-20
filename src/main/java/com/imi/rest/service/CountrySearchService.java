package com.imi.rest.service;

import java.io.IOException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.core.impl.NexmoSearchImpl;
import com.imi.rest.core.impl.PlivioSearchImpl;
import com.imi.rest.core.impl.TwilioSearchImpl;
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryResponse;

@Service
public class CountrySearchService {

    @Autowired
    PlivioSearchImpl plivioCountrySearchImpl;

    @Autowired
    TwilioSearchImpl twilioCountrySearchImpl;

    @Autowired
    NexmoSearchImpl nexmoCountrySearchImpl;
    
    private static final Logger LOG=Logger
            .getLogger(CountrySearchService.class);

    public Set<Country> getCountryListWithISO() throws JsonParseException,
            JsonMappingException, IOException {
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.addCountries(twilioCountrySearchImpl.importCountries());
        countryResponse.addCountries(nexmoCountrySearchImpl.importCountries());
        countryResponse.addCountries(plivioCountrySearchImpl.importCountries());
        return countryResponse.getCountries();
    }
    
}
