package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.core.ReleaseNumber;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivioFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;

@Service
public class ReleaseNumberService implements ReleaseNumber, ProviderConstants {

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;
    @Autowired
    PlivioFactoryImpl plivioFactoryImpl;
    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;

    @Override
    public void releaseNumber(String number, String provider) {

    }

    @Override
    public void releaseNumber(String number, String provider,
            String countryIsoCode) throws ClientProtocolException, IOException {
        switch (provider) {
        case TWILIO:
            twilioFactoryImpl.releaseNumber(number, provider, countryIsoCode);
            break;
        case PLIVIO:
            plivioFactoryImpl.releaseNumber(number, provider, countryIsoCode);
            break;
        case NEXMO:
            nexmoFactoryImpl.releaseNumber(number, provider, countryIsoCode);
            break;
        default:
            break;
        }
    }

}
