package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.exception.ImiException;
import com.imi.rest.exception.InvalidProviderException;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.BalanceResponse;

@Service
public class ProvisionService implements ProviderConstants {
    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;
    @Autowired
    PlivoFactoryImpl plivoFactoryImpl;
    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;
    @Autowired
    ProviderService providerService;

    public ApplicationResponse provisionNumber(String number,
            String countryIsoCode, String providerName,
            ApplicationResponse application)
                    throws ClientProtocolException, IOException, ImiException {
        ApplicationResponse applicationResponse = new ApplicationResponse();
        if (providerName.equalsIgnoreCase(PLIVO)) {
            applicationResponse = plivoFactoryImpl.updateNumber(number,
                    application, providerService.getPlivioProvider());
        } else if (providerName.equalsIgnoreCase(TWILIO)) {
            applicationResponse = twilioFactoryImpl.updateNumber(number,
                    application, providerService.getTwilioProvider());
        } else if (providerName.equalsIgnoreCase(NEXMO)) {
            applicationResponse = nexmoFactoryImpl.updateNumber(number,
                    countryIsoCode, applicationResponse,
                    providerService.getNexmoProvider());
        } else {
            throw new InvalidProviderException(providerName);
        }
        return applicationResponse;
    }

    public ApplicationResponse provisionAllNumbers(String providerName,
            ApplicationResponse application)
                    throws ImiException, ClientProtocolException, IOException {
        ApplicationResponse applicationResponse = new ApplicationResponse();
        if (providerName.equalsIgnoreCase(PLIVO)) {
            applicationResponse = plivoFactoryImpl.updateAllNumbers(application,
                    providerService.getPlivioProvider());
        } else {
            throw new InvalidProviderException(providerName);
        }
        return applicationResponse;
    }
}
