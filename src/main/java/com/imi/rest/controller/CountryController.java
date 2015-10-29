package com.imi.rest.controller;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public CountryResponse countryListResponse()
            throws JsonParseException, JsonMappingException, IOException {
        CountryResponse countryResponse = new CountryResponse();
        Set<Country> countrySet = countrySearchService.getCountryListWithISO();
        countryResponse.setMeta(new MetaForCountries());
        countryResponse.addCountries(countrySet);
        return countryResponse;
    }
    
    @RequestMapping(value="/CountryById")
    public CountryResponse countryById(@RequestHeader("Id") Integer Id){
    	com.imi.rest.db.model.Country country= countrySearchService.getCountryById(Id);
    	CountryResponse countryResponse= new CountryResponse(country);
    	return countryResponse;
    }
    
    @RequestMapping(value="/CountryByName")
    public CountryResponse countryByName(@RequestHeader("Country") String countryName){
    	com.imi.rest.db.model.Country country= countrySearchService.getCountryByName(countryName);
    	CountryResponse countryResponse= new CountryResponse(country);
    	return countryResponse;
    }
    
    @RequestMapping(value="/CountryByIso")
    public CountryResponse countryByIso(@RequestHeader("CountryIsoCode") String countryIsoCode){
    	com.imi.rest.db.model.Country country= countrySearchService.getCountryById(countryIsoCode);
    	CountryResponse countryResponse= new CountryResponse(country);
    	return countryResponse;
    }

}
