package com.imi.rest.controller;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.model.PurchaseDetails;

@RestController
public class PurchaseController {
	
	@RequestMapping(value="/purchase/{number}", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public PurchaseDetails purchaseNumber(@PathVariable("number") String number, @RequestHeader("provider") String providerId, @RequestBody PurchaseDetails purchaseDetails ){
		
		PurchaseDetails purchaseDetails2 = new PurchaseDetails();
		return purchaseDetails2;
	}

}
