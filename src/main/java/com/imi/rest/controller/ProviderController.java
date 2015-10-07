package com.imi.rest.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.model.GenericResponse;
import com.imi.rest.model.Meta;

@RestController
public class ProviderController {
	
	@RequestMapping("/provider")
	public GenericResponse providerResponse(@RequestHeader("provider")String providerId) {
		GenericResponse genResponse=new GenericResponse();
		genResponse.setMeta(new Meta());
		genResponse.setObject(providerId);
		return genResponse;
	}
}