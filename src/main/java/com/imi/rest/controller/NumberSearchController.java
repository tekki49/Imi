package com.imi.rest.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberSearchController {

	@RequestMapping("/PhoneNumber/{countryIsoCode}/{numberType}/{serviceType}/{pattern}")
	public String numberSearchResponse(
			@PathVariable("countryIsoCode") String countryIsoCode,
			@PathVariable("numberType") String numberType,
			@PathVariable("serviceType") String serviceType,
			@PathVariable("pattern") String pattern) {
		return countryIsoCode;
	}

}
