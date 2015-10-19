package com.imi.rest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.NumberSearchService;
import com.imi.rest.model.GenericResponse;
import com.imi.rest.model.Meta;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;

@RestController
public class NumberSearchController {

    private static final Logger LOG = Logger
            .getLogger(NumberSearchController.class);

    @Autowired
    NumberSearchService numberSearchService;

    @RequestMapping(value = "/PhoneNumber/{providerId}/{countryIsoCode}/{numberType}/{serviceType}/{pattern}", method = RequestMethod.GET)
    public GenericResponse numberSearchResponseByProviderId(
            @PathVariable("countryIsoCode") final String countryIsoCode,
            @PathVariable("providerId") final String providerId,
            @PathVariable("numberType") final String numberType,
            @PathVariable("serviceType") final String serviceType,
            @PathVariable("pattern") final String pattern) {
        GenericResponse genResponse = new GenericResponse();
        ServiceConstants serviceTypeEnum = ServiceConstants
                .evaluate(serviceType);
        try {
            numberSearchService.searchPhoneNumbers(serviceTypeEnum, providerId,
                    countryIsoCode, numberType, pattern);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        genResponse.setMeta(new Meta());
        genResponse.setObject(new ArrayList<Number>());
        return genResponse;
    }

    @RequestMapping(value = "/PhoneNumber/{countryIsoCode}/{numberType}/{serviceType}/{pattern}", method = RequestMethod.GET)
    public NumberResponse numberSearchResponse(
            @PathVariable("countryIsoCode") final String countryIsoCode,
            @PathVariable("numberType") final String numberType,
            @PathVariable("serviceType") final String serviceType,
            @PathVariable("pattern") final String pattern) {
        NumberResponse numberResponse = new NumberResponse();
        ServiceConstants serviceTypeEnum = ServiceConstants
                .evaluate(serviceType);
        List<Number> numberList = null;
        try {
            numberList = numberSearchService.searchPhoneNumbers(
                    serviceTypeEnum, countryIsoCode, numberType, pattern);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        numberResponse.setMeta(new Meta());
        numberResponse.setObjects(numberList);
        return numberResponse;
    }

}
