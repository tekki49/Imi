package com.imi.rest.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.PhoneSearchService;
import com.imi.rest.model.GenericResponse;
import com.imi.rest.model.Meta;
import com.imi.rest.model.NumberResponse;

@RestController
public class NumberSearchController {

	@Autowired
	PhoneSearchService phoneSearchService;

	@RequestMapping(value = "/PhoneNumber/{countryIsoCode}/{providerId}/{numberType}/{serviceType}/{pattern}", method = RequestMethod.GET)
	public GenericResponse numberSearchResponse(
			@PathVariable("countryIsoCode") String countryIsoCode,
			@PathVariable("providerId") String providerId,
			@PathVariable("numberType") String numberType,
			@PathVariable("serviceType") String serviceType,
			@PathVariable("pattern") String pattern) {
		GenericResponse genResponse = new GenericResponse();
		String url = null;
		System.out.println(serviceType);
		ServiceConstants serviceTypeEnum = ServiceConstants
				.evaluate(serviceType);
		try {
			phoneSearchService.searchPhoneNumbers(serviceTypeEnum,providerId,
					countryIsoCode, numberType, pattern);
		} catch (IOException e) {
			e.printStackTrace();
		}
		genResponse.setMeta(new Meta());
		genResponse.setObject(new ArrayList<NumberResponse>());
		return genResponse;
	}

}
