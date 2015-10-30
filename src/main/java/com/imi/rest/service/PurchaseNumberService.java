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
import com.imi.rest.dao.model.Provider;

@Service
public class PurchaseNumberService implements ProviderConstants, UrlConstants {

    @Autowired
    PlivioFactoryImpl plivioFactoryImpl;

    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;

    public void purchaseNumber(String number, Provider provider,
            String countryIsoCode) throws ClientProtocolException, IOException {
        if (provider.getName().equalsIgnoreCase(TWILIO)) {
            twilioFactoryImpl.purchaseNumber(number, provider, countryIsoCode);
        } else if (provider.getName().equalsIgnoreCase(PLIVIO)) {
            plivioFactoryImpl.purchaseNumber(number, provider, countryIsoCode);
        } else if (provider.getName().equalsIgnoreCase(NEXMO)) {
            nexmoFactoryImpl.purchaseNumber(number, provider, countryIsoCode);
        }
    }
}
