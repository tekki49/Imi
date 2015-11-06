package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.exception.InvalidProviderException;
import com.imi.rest.model.NumberResponse;

@Service
public class NumberSearchService implements ProviderConstants {

    @Autowired
    PlivoFactoryImpl plivoFactoryImpl;

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;

    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;

    @Autowired
    ProviderService providerService;

    public NumberResponse searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            Provider provider, String countryIsoCode, String numberType,
            String pattern, String nextPlivoIndex, String nextNexmoIndex)
                    throws ClientProtocolException, IOException, ImiException {
        NumberResponse numberResponse = new NumberResponse();
        if (provider.getName().equalsIgnoreCase(PLIVO)) {
            plivoFactoryImpl.searchPhoneNumbers(
                    providerService.getPlivioProvider(), serviceTypeEnum,
                    countryIsoCode, numberType, pattern, nextPlivoIndex,
                    numberResponse);
        } else if (provider.getName().equalsIgnoreCase(TWILIO)) {
            twilioFactoryImpl.searchPhoneNumbers(
                    providerService.getTwilioProvider(), serviceTypeEnum,
                    countryIsoCode, numberType, pattern, "", numberResponse);
        } else if (provider.getName().equalsIgnoreCase(NEXMO)) {
            nexmoFactoryImpl.searchPhoneNumbers(
                    providerService.getNexmoProvider(), serviceTypeEnum,
                    countryIsoCode, numberType, pattern, nextNexmoIndex,
                    numberResponse);
        } else {
            throw new InvalidProviderException(provider.getName());
        }
        return numberResponse;
    }

    public NumberResponse searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern,
            String nextPlivoIndex, String nextNexmoIndex)
                    throws ClientProtocolException, IOException, ImiException {
        NumberResponse numberResponse = new NumberResponse();
        plivoFactoryImpl.searchPhoneNumbers(providerService.getPlivioProvider(),
                serviceTypeEnum, countryIsoCode, numberType, pattern,
                nextPlivoIndex, numberResponse);
        twilioFactoryImpl.searchPhoneNumbers(
                providerService.getTwilioProvider(), serviceTypeEnum,
                countryIsoCode, numberType, pattern, "", numberResponse);
        nexmoFactoryImpl.searchPhoneNumbers(providerService.getNexmoProvider(),
                serviceTypeEnum, countryIsoCode, numberType, pattern,
                nextNexmoIndex, numberResponse);
        return numberResponse;
    }

}