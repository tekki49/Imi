package com.imi.rest.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Purchase;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.PurchaseDetails;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.ProviderService;
import com.imi.rest.service.PurchaseNumberService;
import com.imi.rest.service.PurchaseService;

@RestController
public class PurchaseController {

    @Autowired
    PurchaseNumberService purchaseNumberService;
    @Autowired
    ProviderService providerService;
    @Autowired
    PurchaseService purchaseService;

    @RequestMapping(value = "/purchase/{number}/{countryIsoCode}", method = RequestMethod.POST)
    public PurchaseDetails purchaseNumber(@PathVariable("number") String number,
            @PathVariable("countryIsoCode") String countryIsoCode,
            @RequestHeader("provider") int providerId)
                    throws ClientProtocolException, IOException, ImiException {
        Provider provider = providerService.getProviderById(providerId);
        purchaseNumberService.purchaseNumber(number, provider,
                countryIsoCode);
        PurchaseDetails purchaseDetails2 = new PurchaseDetails();
        return purchaseDetails2;
    }

    @RequestMapping(value = "/purchaseByNumber", method = RequestMethod.POST)
    public PurchaseResponse puchaseByNumber(
            @RequestHeader("number") Integer number) {
        Purchase purchase = purchaseService.getPurchaseByNumber(number);
        return new PurchaseResponse(purchase);
    }
}