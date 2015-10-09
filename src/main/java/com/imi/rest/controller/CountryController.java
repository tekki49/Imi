package com.imi.rest.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.model.CountryResponse;
import com.imi.rest.model.GenericResponse;
import com.imi.rest.model.Meta;

@RestController
public class CountryController {
	
	@RequestMapping(value="/Countries",method=RequestMethod.GET)
	public GenericResponse countryListResponse() {
		GenericResponse genResponse=new GenericResponse();
		genResponse.setMeta(new Meta());
		genResponse.setObject(new ArrayList<CountryResponse>());
		return genResponse;
	}

}
