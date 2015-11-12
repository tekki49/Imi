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
    
    @RequestMapping(value = "/application/update", method = RequestMethod.POST)
    public ApplicationResponse updateApplication(
            @RequestHeader("provider") String providerName,
            @RequestBody ApplicationResponse application)
                    throws ClientProtocolException, IOException, ImiException {
        ApplicationResponse applicationResponse = provisionService
                .updateApplication(providerName, application);
        return applicationResponse;
    }
    
    @RequestMapping(value = "/application/create", method = RequestMethod.POST)
    public ApplicationResponse createApplication(
            @RequestHeader("provider") String providerName,
            @RequestBody ApplicationResponse application)
                    throws ClientProtocolException, IOException, ImiException {
        ApplicationResponse applicationResponse = provisionService
                .createApplication(providerName, application);
        return applicationResponse;
    }
    
    @RequestMapping(value = "/application/get/{app_id}", method = RequestMethod.GET)
    public ApplicationResponse getApplication(@PathVariable("app_id") String app_id,
            @RequestHeader("provider") String providerName )
                    throws ClientProtocolException, IOException, ImiException {
        ApplicationResponse applicationResponse = provisionService
                .getApplication(providerName, app_id);
        return applicationResponse;
    }    
}
