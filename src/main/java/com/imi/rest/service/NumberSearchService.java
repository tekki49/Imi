package com.imi.rest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.impl.NexmoSearchImpl;
import com.imi.rest.core.impl.PlivioSearchImpl;
import com.imi.rest.core.impl.TwilioSearchImpl;
import com.imi.rest.model.Number;

@Service
public class NumberSearchService implements ProviderConstants {

    @Autowired
    PlivioSearchImpl plivioNumberSearchImpl;

    @Autowired
    TwilioSearchImpl twilioNumberSearchImpl;

    @Autowired
    NexmoSearchImpl nexmoNumberSearchImpl;

    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String provider, String countryIsoCode, String numberType,
            String pattern) throws ClientProtocolException, IOException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        if (provider.equalsIgnoreCase(PLIVIO)) {
            phoneSearchResult.addAll(plivioNumberSearchImpl.searchPhoneNumbers(
                    serviceTypeEnum, countryIsoCode, numberType, pattern));
        } else if (provider.equalsIgnoreCase(TWILIO)) {
            phoneSearchResult.addAll(twilioNumberSearchImpl.searchPhoneNumbers(
                    serviceTypeEnum, countryIsoCode, numberType, pattern));
        } else if (provider.equalsIgnoreCase(NEXMO)) {
            phoneSearchResult.addAll(nexmoNumberSearchImpl.searchPhoneNumbers(
                    serviceTypeEnum, countryIsoCode, numberType, pattern));
        }
        return phoneSearchResult;
    }

    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern)
                    throws ClientProtocolException, IOException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        phoneSearchResult.addAll(plivioNumberSearchImpl.searchPhoneNumbers(
                serviceTypeEnum, countryIsoCode, numberType, pattern));
        phoneSearchResult.addAll(twilioNumberSearchImpl.searchPhoneNumbers(
                serviceTypeEnum, countryIsoCode, numberType, pattern));
        phoneSearchResult.addAll(nexmoNumberSearchImpl.searchPhoneNumbers(
                serviceTypeEnum, countryIsoCode, numberType, pattern));
        return phoneSearchResult;
    }

}