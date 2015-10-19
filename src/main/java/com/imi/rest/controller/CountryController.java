package com.imi.rest.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.core.CountrySearchService;
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryResponse;
import com.imi.rest.model.GenericResponse;

@RestController
public class CountryController {

    @Autowired
    CountrySearchService countrySearchService;

    @RequestMapping(value = "/Countries", method = RequestMethod.GET)
    public CountryResponse countryListResponse() throws JsonParseException,
            JsonMappingException, IOException {
        GenericResponse genResponse = new GenericResponse();
        // genResponse.setMeta(new Meta());
        // genResponse.setObject(new ArrayList<CountryResponse>());
        countrySearchService.getCountryListWithISO();
        CountryResponse countryResponse = new CountryResponse();
        countryResponse = countrySearchService.getCountryListWithISO();
        return countryResponse;
    }

}
