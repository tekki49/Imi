package com.imi.rest.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.SubAccountDetails;
import com.imi.rest.service.ProviderService;
import com.imi.rest.service.SubAccountService;

@RestController
public class SubAccountController {
	
	@Autowired
	SubAccountService subAccountService;
	@Autowired
	ProviderService providerService;
	
	@RequestMapping(value = "/createSubAccount/{friendlyName}/{providerId}", method = RequestMethod.POST)
	public SubAccountDetails createNewSubAccount(
			@PathVariable("friendlyName") final String friendlyName,
			@PathVariable("providerId") final Integer providerId) throws ImiException, JsonParseException, JsonMappingException, IOException{
		Provider provider= providerService.getProviderById(providerId);
		SubAccountDetails subAccountDetails=subAccountService.createNewSubAccount(friendlyName,provider);
		return subAccountDetails;
	}
	
	@RequestMapping(value = "/createSubAccount/{friendlyName}/{providerId}", method = RequestMethod.GET)
	public SubAccountDetails getSubAccountDetails(
			@PathVariable("friendlyName") final String friendlyName,
			@PathVariable("providerId") final Integer providerId) throws ImiException, JsonParseException, JsonMappingException, IOException{
		Provider provider= providerService.getProviderById(providerId);
		SubAccountDetails subAccountDetails=subAccountService.getSubAccountDetailsByFriendlyName(friendlyName,provider);
		return subAccountDetails;
	}

}
