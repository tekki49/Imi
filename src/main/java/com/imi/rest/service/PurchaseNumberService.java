package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.PurchaseResponse;

@Service
public class PurchaseNumberService implements ProviderConstants, UrlConstants {

    @Autowired
    PlivoFactoryImpl plivioFactoryImpl;

    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;

    public PurchaseResponse purchaseNumber(String number, String numberType,
            Provider provider, Country country,
            ServiceConstants serviceTypeEnum)
                    throws ClientProtocolException, IOException, ImiException {
        if (provider.getName().equalsIgnoreCase(TWILIO)) {
            return twilioFactoryImpl.purchaseNumber(number, numberType,
                    provider, country, serviceTypeEnum);
        } else if (provider.getName().equalsIgnoreCase(PLIVO)) {
            return plivioFactoryImpl.purchaseNumber(number, numberType,
                    provider, country, serviceTypeEnum);
        } else if (provider.getName().equalsIgnoreCase(NEXMO)) {
            return nexmoFactoryImpl.purchaseNumber(number, numberType, provider,
                    country, serviceTypeEnum);
        } else if (provider.getName().equalsIgnoreCase(TWILIO_DUMMY)) {
            return twilioFactoryImpl.purchaseNumber(number, numberType,
                    provider, country, serviceTypeEnum);
        } else {
            return null;
        }
    }
}
