package com.imi.rest.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.model.GenericResponse;
import com.imi.rest.model.Meta;
import com.imi.rest.model.PurchaseDetails;

@RestController
public class NumberSearchController {

	@RequestMapping("/PhoneNumber/{countryIsoCode}/{providerId}/{numberType}/{serviceType}/{pattern}")
	public GenericResponse numberSearchResponse(
			@PathVariable("countryIsoCode") String countryIsoCode,
			@PathVariable("providerId") String providerId,
			@PathVariable("numberType") String numberType,
			@PathVariable("serviceType") String serviceType,
			@PathVariable("pattern") String pattern) {
		
		GenericResponse genResponse = new GenericResponse();
		genResponse.setMeta(new Meta());
		genResponse.setObject(new PurchaseDetails());
		
		return genResponse;
	}

}
