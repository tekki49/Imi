package com.imi.rest.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Purchase;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.PurchaseDetails;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.CountrySearchService;
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
    @Autowired
    CountrySearchService countrySearchService;

    @RequestMapping(value = "/purchase/{number}/{countryIsoCode}", method = RequestMethod.POST)
    public PurchaseDetails purchaseNumber(@PathVariable("number") String number,
            @PathVariable("countryIsoCode") String countryIsoCode,
            @RequestHeader("provider") String providerName)
                    throws ClientProtocolException, IOException, ImiException {
        Provider provider = providerService.getProviderByName(providerName);
        Country country=countrySearchService.getCountryByIsoCode(countryIsoCode);
        purchaseNumberService.purchaseNumber(number, provider, country.getCountryIsoNumber());
        PurchaseDetails purchaseDetails = new PurchaseDetails();
        purchaseDetails.setCountry(country.getCountry());
        purchaseDetails.setNumber(number);
        return purchaseDetails;
    }

    @RequestMapping(value = "/purchaseByNumber", method = RequestMethod.POST)
    public PurchaseResponse puchaseByNumber(
            @RequestHeader("number") Integer number) {
        Purchase purchase = purchaseService.getPurchaseByNumber(number);
        return new PurchaseResponse(purchase);
    }
}