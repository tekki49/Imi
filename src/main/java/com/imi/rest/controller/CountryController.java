package com.imi.rest.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.db.model.Countries;
import com.imi.rest.model.GenericResponse;
import com.imi.rest.model.Meta;

@RestController
public class CountryController {
	
	@RequestMapping("/Countries")
	public GenericResponse countryListResponse() {
		GenericResponse genResponse=new GenericResponse();
		genResponse.setMeta(new Meta());
		genResponse.setObject(new ArrayList<Countries>());
		return genResponse;
	}

}
