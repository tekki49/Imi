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
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.exception.InvalidProviderException;
import com.imi.rest.model.Number;

@Service
public class NumberSearchService implements ProviderConstants {

    @Autowired
    PlivioFactoryImpl plivioFactoryImpl;

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;

    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;
    
    @Autowired
    ProviderService providerService;

    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            Provider provider, String countryIsoCode, String numberType,
            String pattern)
                    throws ClientProtocolException, IOException, ImiException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        if (provider.getName().equalsIgnoreCase(PLIVIO)) {
            phoneSearchResult.addAll(plivioFactoryImpl.searchPhoneNumbers(
                    provider, serviceTypeEnum, countryIsoCode, numberType,
                    pattern));
        } else if (provider.getName().equalsIgnoreCase(TWILIO)) {
            phoneSearchResult.addAll(twilioFactoryImpl.searchPhoneNumbers(
                    provider, serviceTypeEnum, countryIsoCode, numberType,
                    pattern));
        } else if (provider.getName().equalsIgnoreCase(NEXMO)) {
            phoneSearchResult.addAll(nexmoFactoryImpl.searchPhoneNumbers(
                    provider, serviceTypeEnum, countryIsoCode, numberType,
                    pattern));
        } else {
            throw new InvalidProviderException(provider.getName());
        }
        return phoneSearchResult;
    }

    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern)
                    throws ClientProtocolException, IOException, ImiException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        phoneSearchResult.addAll(plivioFactoryImpl.searchPhoneNumbers(providerService.getProviderByName(PLIVIO),
                serviceTypeEnum, countryIsoCode, numberType, pattern));
        phoneSearchResult.addAll(twilioFactoryImpl.searchPhoneNumbers(providerService.getProviderByName(TWILIO),
                serviceTypeEnum, countryIsoCode, numberType, pattern));
        phoneSearchResult.addAll(nexmoFactoryImpl.searchPhoneNumbers(providerService.getProviderByName(NEXMO),
                serviceTypeEnum, countryIsoCode, numberType, pattern));
        return phoneSearchResult;
    }

}