package com.imi.rest.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.service.NumberSearchService;
import com.imi.rest.service.ProviderService;

@RestController
public class NumberSearchController {

	private static final Logger LOG = Logger.getLogger(NumberSearchController.class);

	@Autowired
	NumberSearchService numberSearchService;

	@Autowired
	ProviderService providerService;

	@RequestMapping(value = "/PhoneNumber/{providerId}/{countryIsoCode}/{numberType}/{serviceType}/{pattern}", method = RequestMethod.GET)
	public NumberResponse numberSearchResponseByProviderId(@PathVariable("countryIsoCode") final String countryIsoCode,
			@PathVariable("provider") final String provider, @PathVariable("numberType") final String numberType,
			@PathVariable("serviceType") final String serviceType, @PathVariable("pattern") final String pattern)
					throws ImiException {
		NumberResponse numberResponse = null;
		ServiceConstants serviceTypeEnum = ServiceConstants.evaluate(serviceType);
		try {
			numberResponse = numberSearchService.searchPhoneNumbers(serviceTypeEnum,
					providerService.getProviderByName(provider), countryIsoCode, numberType, pattern, "FIRST", "FIRST");
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return numberResponse;
	}

	@RequestMapping(value = "/PhoneNumber/{countryIsoCode}/{numberType}/{serviceType}/{pattern}", method = RequestMethod.GET)
	public NumberResponse numberSearchResponse(@PathVariable("countryIsoCode") final String countryIsoCode,
			@PathVariable("numberType") final String numberType, @PathVariable("serviceType") final String serviceType,
			@PathVariable("pattern") final String pattern) throws ImiException {
		NumberResponse numberResponse = null;
		ServiceConstants serviceTypeEnum = ServiceConstants.evaluate(serviceType);
		try {
			numberResponse = numberSearchService.searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType,
					pattern, "FIRST", "FIRST");
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return numberResponse;
	}

	@RequestMapping(value = "/PhoneNumber/{countryIsoCode}/{numberType}/{serviceType}", method = RequestMethod.GET)
	public NumberResponse numberSearchResponse(@PathVariable("countryIsoCode") final String countryIsoCode,
			@PathVariable("numberType") final String numberType, @PathVariable("serviceType") final String serviceType)
					throws ImiException {
		NumberResponse numberResponse = null;
		ServiceConstants serviceTypeEnum = ServiceConstants.evaluate(serviceType);
		try {
			numberResponse = numberSearchService.searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType, "",
					"FIRST", "FIRST");
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return numberResponse;
	}

	@RequestMapping(value = "/PhoneNumber/{countryIsoCode}/{numberType}/{serviceType}/{pattern}/{nextPlivoIndex}/{nextNexmoIndex}", method = RequestMethod.GET)
	public NumberResponse nextNumberSearchResponse(@PathVariable("countryIsoCode") final String countryIsoCode,
			@PathVariable("numberType") final String numberType, @PathVariable("serviceType") final String serviceType,
			@PathVariable("nextPlivoIndex") final String nextPlivoIndex, @PathVariable("pattern") final String pattern,
			@PathVariable("nextNexmoIndex") final String nextNexmoIndex) throws ImiException {
		NumberResponse numberResponse = null;
		ServiceConstants serviceTypeEnum = ServiceConstants.evaluate(serviceType);
		try {
			if (pattern.equals("@")) {
				numberResponse = numberSearchService.searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType, "",
						nextPlivoIndex, nextNexmoIndex);

			} else {
				numberResponse = numberSearchService.searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType,
						pattern, nextPlivoIndex, nextNexmoIndex);
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return numberResponse;
	}
}
