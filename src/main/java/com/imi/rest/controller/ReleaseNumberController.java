package com.imi.rest.controller;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imi.rest.core.ReleaseNumber;
import com.imi.rest.helper.ApplicationHelper;
import com.imi.rest.service.ProviderService;
import com.imi.rest.util.ImiJsonUtil;

@RestController
public class ReleaseNumberController {

	@Autowired
	ReleaseNumber releaseNumberService;
	
	@Autowired
	ProviderService providerService;
	
    @RequestMapping(value = "/release/{number}", method = RequestMethod.DELETE)
    public String releaseNumber(@PathVariable("number") String number,
    		@PathVariable("countryIsoCode") String countryIsoCode,
            @RequestHeader("provider") String providerId)
            throws ClientProtocolException, IOException {
    	List<String> errors = ApplicationHelper.validatePurchaseRequest(number, providerId);
     	String provider = providerService.getProviderById(providerId);
    	releaseNumberService.releaseNumber(number, provider, countryIsoCode);
        return ImiJsonUtil.getJSONString(number, "released");
    }

}


