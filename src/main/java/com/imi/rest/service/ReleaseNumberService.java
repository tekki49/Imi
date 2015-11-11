package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.exception.InvalidProviderException;

@Service
public class ReleaseNumberService implements ProviderConstants {

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;
    @Autowired
    PlivoFactoryImpl plivioFactoryImpl;
    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode)
                    throws ClientProtocolException, IOException, ImiException {
        if (provider.getName().equalsIgnoreCase(TWILIO)) {
            twilioFactoryImpl.releaseNumber(number, provider, countryIsoCode);
        } else if (provider.getName().equalsIgnoreCase(PLIVO)) {
            plivioFactoryImpl.releaseNumber(number, provider, countryIsoCode);
        } else if (provider.getName().equalsIgnoreCase(NEXMO)) {
            nexmoFactoryImpl.releaseNumber(number, provider, countryIsoCode);
        } else {
            throw new InvalidProviderException(provider.getName());
        }
    }

}
