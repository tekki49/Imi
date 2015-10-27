package com.imi.rest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivioFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.model.Number;

@Service
public class NumberSearchService implements ProviderConstants {

    @Autowired
    PlivioFactoryImpl plivioFactoryImpl;

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;

    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;

    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String provider, String countryIsoCode, String numberType,
            String pattern) throws ClientProtocolException, IOException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        if (provider.equalsIgnoreCase(PLIVIO)) {
            phoneSearchResult.addAll(plivioFactoryImpl.searchPhoneNumbers(
                    serviceTypeEnum, countryIsoCode, numberType, pattern));
        } else if (provider.equalsIgnoreCase(TWILIO)) {
            phoneSearchResult.addAll(twilioFactoryImpl.searchPhoneNumbers(
                    serviceTypeEnum, countryIsoCode, numberType, pattern));
        } else if (provider.equalsIgnoreCase(NEXMO)) {
            phoneSearchResult.addAll(nexmoFactoryImpl.searchPhoneNumbers(
                    serviceTypeEnum, countryIsoCode, numberType, pattern));
        }
        return phoneSearchResult;
    }

    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern)
                    throws ClientProtocolException, IOException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        phoneSearchResult.addAll(plivioFactoryImpl.searchPhoneNumbers(
                serviceTypeEnum, countryIsoCode, numberType, pattern));
        phoneSearchResult.addAll(twilioFactoryImpl.searchPhoneNumbers(
                serviceTypeEnum, countryIsoCode, numberType, pattern));
        phoneSearchResult.addAll(nexmoFactoryImpl.searchPhoneNumbers(
                serviceTypeEnum, countryIsoCode, numberType, pattern));
        return phoneSearchResult;
    }

}