package com.imi.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.ProviderResponse;
import com.imi.rest.service.ProviderService;

@RestController
public class ProviderController {

	private static final Logger LOG = Logger.getLogger(ProviderController.class);

	@Autowired
	ProviderService providerService;

	@RequestMapping(value = "/provider", method = RequestMethod.GET)
	public ProviderResponse providerResponse(@RequestHeader("provider") int providerId) {
		LOG.info("Inside ProviderController");
		Provider provider = providerService.getProviderById(providerId);
		ProviderResponse providerResponse = new ProviderResponse(provider);
		return providerResponse;
	}
}