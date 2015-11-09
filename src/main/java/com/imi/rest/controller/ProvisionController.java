package com.imi.rest.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.exception.ImiException;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.service.ProvisionService;

@RestController
public class ProvisionController {

    @Autowired
    ProvisionService provisionService;

    @RequestMapping(value = "/number/update/{number}", method = RequestMethod.POST)
    public ApplicationResponse provisionNumber(
            @PathVariable("number") String number,
            @RequestHeader("provider") String providerName,
            @RequestBody ApplicationResponse application)
                    throws ClientProtocolException, IOException, ImiException {
        ApplicationResponse applicationResponse = provisionService
                .provisionNumber(number, "", providerName, application);
        return applicationResponse;
    }
    @RequestMapping(value = "/number/updateAll", method = RequestMethod.POST)
    public ApplicationResponse provisionNumbers(
            @RequestHeader("provider") String providerName,
            @RequestBody ApplicationResponse application)
                    throws ClientProtocolException, IOException, ImiException {
        ApplicationResponse applicationResponse = provisionService
                .provisionAllNumbers(providerName, application);
        return applicationResponse;
    }
}
