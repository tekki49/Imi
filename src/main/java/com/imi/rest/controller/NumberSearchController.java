package com.imi.rest.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.model.GenericResponse;
import com.imi.rest.model.Meta;
import com.imi.rest.model.NumberResponse;

@RestController
public class NumberSearchController {

	@RequestMapping(value = "/PhoneNumber/{countryIsoCode}/{numberType}/{serviceType}/{pattern}", method = RequestMethod.GET)
	public GenericResponse numberSearchResponse(
			@PathVariable("countryIsoCode") String countryIsoCode,
			@PathVariable("numberType") String numberType,
			@PathVariable("serviceType") String serviceType,
			@PathVariable("pattern") String pattern) {
		GenericResponse genResponse = new GenericResponse();
		genResponse.setMeta(new Meta());
		genResponse.setObject(new ArrayList<NumberResponse>());
		return genResponse;
	}

}
