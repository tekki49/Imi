package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivioFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;

@Service
public class PurchaseNumberService implements ProviderConstants, UrlConstants {

    @Autowired
    PlivioFactoryImpl plivioFactoryImpl;

    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;

    public void purchaseNumber(String number, String provider,
            String countryIsoCode) throws ClientProtocolException, IOException {
        switch (provider) {
        case TWILIO:
            twilioFactoryImpl.purchaseNumber(number, provider, countryIsoCode);
            break;
        case PLIVIO:
            plivioFactoryImpl.purchaseNumber(number, provider, countryIsoCode);
            break;
        case NEXMO:
            nexmoFactoryImpl.purchaseNumber(number, provider, countryIsoCode);
            break;
        default:
            break;
        }
    }
}
