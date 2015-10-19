package com.imi.rest.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.db.model.Providers;
import com.imi.rest.model.ProviderResponse;

@RestController
public class ProviderController {

    @RequestMapping(value = "/provider", method = RequestMethod.GET)
    public ProviderResponse providerResponse(
            @RequestHeader("provider") String providerId) {
        return new ProviderResponse(new Providers());
    }
}