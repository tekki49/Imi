package com.imi.rest.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.imi.rest.model.Client;
import com.imi.rest.model.GenericResponse;
import com.imi.rest.model.Meta;

@RestController
public class ClientController {
	
	@RequestMapping(value="/client",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse clientListResponse(@RequestHeader("api_key")String apiKey,
			@RequestHeader("auth_Id")String authId,
			@RequestHeader("provider")String providerId,
			@RequestBody Client client) {
		GenericResponse genResponse=new GenericResponse();
		genResponse.setMeta(new Meta());
		genResponse.setObject(client);
		return genResponse;
	}

}
