package com.imi.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.model.ClientRequest;
import com.imi.rest.model.ClientResponse;

@RestController
public class ClientController {
	
	@RequestMapping(value="/client",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ClientResponse clientListResponse(@RequestHeader("api_key")String apiKey,
			@RequestHeader("auth_Id")String authId,
			@RequestHeader("provider")String providerId,
			@RequestBody ClientRequest client) {
		return new ClientResponse();
	}

}
