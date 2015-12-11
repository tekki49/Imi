package com.imi.rest.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.service.NumberSearchService;
import com.imi.rest.service.ProviderService;
import com.imi.rest.util.ImiDataFormatUtils;

@RestController
public class NumberSearchController {

    private static final Logger LOG = Logger
            .getLogger(NumberSearchController.class);

    @Autowired
    NumberSearchService numberSearchService;

    @Autowired
    ProviderService providerService;

    @RequestMapping(value = "/PhoneNumber/{provider}/{countryIsoCode}/{numberType}/{serviceType}/{pattern}", method = RequestMethod.GET)
    public NumberResponse numberSearchResponseByProviderId(
            @PathVariable("countryIsoCode") final String countryIsoCode,
            @PathVariable("provider") final String provider,
            @PathVariable("numberType") final String numberType,
            @PathVariable("serviceType") final String serviceType,
            @PathVariable("pattern") final String pattern,
            @RequestHeader(value = "userid", defaultValue = "0") Integer userid,
            @RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
            @RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
            @RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
            @RequestHeader(value = "clientname", defaultValue = "0") String clientname,
            @RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
            @RequestHeader(value = "markup", defaultValue = "0") String markup)
             {
        LOG.info("in NumberSearchController");
        NumberResponse numberResponse = null;
        ServiceConstants serviceTypeEnum = ServiceConstants
                .evaluate(serviceType);
        Provider providerObj = null;
        if (ImiDataFormatUtils.isNumber(provider)) {
            providerObj = providerService.getProviderById(Integer
                    .parseInt(provider));
        } else {
            providerObj = providerService.getProviderByName(provider);
        }
        try {
            numberResponse = numberSearchService.searchPhoneNumbers(
                    serviceTypeEnum, providerObj, countryIsoCode, numberType,
                    pattern, "FIRST", "FIRST", markup);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return numberResponse;
    }

    @RequestMapping(value = "/PhoneNumber/{countryIsoCode}/{numberType}/{serviceType}/{pattern}", method = RequestMethod.GET)
    public NumberResponse numberSearchResponse(
            @PathVariable("countryIsoCode") final String countryIsoCode,
            @PathVariable("numberType") final String numberType,
            @PathVariable("serviceType") final String serviceType,
            @PathVariable("pattern") final String pattern,
            @RequestHeader(value = "userid", defaultValue = "0") Integer userid,
            @RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
            @RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
            @RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
            @RequestHeader(value = "clientname", defaultValue = "0") String clientname,
            @RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
            @RequestHeader(value = "markup", defaultValue = "0") String markup)
            {
        LOG.info("in NumberSearchController");
        NumberResponse numberResponse = null;
        ServiceConstants serviceTypeEnum = ServiceConstants
                .evaluate(serviceType);
        try {
            numberResponse = numberSearchService.searchPhoneNumbers(
                    serviceTypeEnum, countryIsoCode, numberType, pattern,
                    "FIRST", "FIRST", markup);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return numberResponse;
    }

    @RequestMapping(value = "/PhoneNumber/{countryIsoCode}/{numberType}/{serviceType}", method = RequestMethod.GET)
    public NumberResponse numberSearchResponse(
            @PathVariable("countryIsoCode") final String countryIsoCode,
            @PathVariable("numberType") final String numberType,
            @PathVariable("serviceType") final String serviceType,
            @RequestHeader(value = "userid", defaultValue = "0") Integer userid,
            @RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
            @RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
            @RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
            @RequestHeader(value = "clientname", defaultValue = "0") String clientname,
            @RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
            @RequestHeader(value = "markup", defaultValue = "0") String markup)
           {
        LOG.info("in NumberSearchController");
        NumberResponse numberResponse = null;
        ServiceConstants serviceTypeEnum = ServiceConstants
                .evaluate(serviceType);
        try {
            numberResponse = numberSearchService.searchPhoneNumbers(
                    serviceTypeEnum, countryIsoCode, numberType, "", "FIRST",
                    "FIRST", markup);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return numberResponse;
    }

    @RequestMapping(value = "/PhoneNumber/{countryIsoCode}/{numberType}/{serviceType}/{pattern}/{nextPlivoIndex}/{nextNexmoIndex}", method = RequestMethod.GET)
    public NumberResponse nextNumberSearchResponse(
            @PathVariable("countryIsoCode") final String countryIsoCode,
            @PathVariable("numberType") final String numberType,
            @PathVariable("serviceType") final String serviceType,
            @PathVariable("pattern") final String pattern,
            @PathVariable("nextPlivoIndex") final String nextPlivoIndex,
            @PathVariable("nextNexmoIndex") final String nextNexmoIndex,
            @RequestHeader(value = "userid", defaultValue = "0") Integer userid,
            @RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
            @RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
            @RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
            @RequestHeader(value = "clientname", defaultValue = "0") String clientname,
            @RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
            @RequestHeader(value = "markup", defaultValue = "0") String markup)
            {
        LOG.info("in NumberSearchController");
        NumberResponse numberResponse = null;
        ServiceConstants serviceTypeEnum = ServiceConstants
                .evaluate(serviceType);
        try {
            if (pattern.equals("@")) {
                numberResponse = numberSearchService.searchPhoneNumbers(
                        serviceTypeEnum, countryIsoCode, numberType, "",
                        nextPlivoIndex, nextNexmoIndex, markup);

            } else {
                numberResponse = numberSearchService.searchPhoneNumbers(
                        serviceTypeEnum, countryIsoCode, numberType, pattern,
                        nextPlivoIndex, nextNexmoIndex, markup);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return numberResponse;
    }
}
