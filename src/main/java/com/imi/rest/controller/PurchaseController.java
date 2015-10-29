package com.imi.rest.controller;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.helper.ApplicationHelper;
import com.imi.rest.model.PurchaseDetails;
import com.imi.rest.service.ProviderService;
import com.imi.rest.service.PurchaseNumberService;

@RestController
public class PurchaseController {

    @Autowired
    PurchaseNumberService purchaseNumberService;
    @Autowired
    ProviderService providerService;

    @RequestMapping(value = "/purchase/{number}/{countryIsoCode}", method = RequestMethod.POST)
    public PurchaseDetails purchaseNumber(@PathVariable("number") String number,
            @PathVariable("countryIsoCode") String countryIsoCode,
            @RequestHeader("provider") String providerId)
                    throws ClientProtocolException, IOException {
        List<String> errors = ApplicationHelper.validatePurchaseRequest(number,
                providerId);
        // if (!errors.isEmpty()){
        // return new
        // ResponseEntity<PurchaseDetails>(HttpStatus.SC_BAD_REQUEST);
        // }
        // else{
        String provider = providerService.getProviderById(providerId);
        purchaseNumberService.purchaseNumber(number, provider, countryIsoCode);
        PurchaseDetails purchaseDetails2 = new PurchaseDetails();
        return purchaseDetails2;
    }

}