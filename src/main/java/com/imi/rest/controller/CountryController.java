package com.imi.rest.controller;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryResponse;
import com.imi.rest.model.MetaForCountries;
import com.imi.rest.service.CountrySearchService;

@RestController
public class CountryController {

    @Autowired
    CountrySearchService countrySearchService;

    @RequestMapping(value = "/Countries", method = RequestMethod.GET)
    public CountryResponse countryListResponse() throws JsonParseException,
            JsonMappingException, IOException {
        countrySearchService.getCountryListWithISO();
        CountryResponse countryResponse = new CountryResponse();
        Set<Country> countrySet = countrySearchService.getCountryListWithISO();
        countryResponse.setMeta(new MetaForCountries());
        countryResponse.addCountries(countrySet);
        return countryResponse;
    }

}
