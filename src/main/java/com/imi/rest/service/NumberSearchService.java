package com.imi.rest.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.Number;
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

    @Value(value = "${search.skip.twilio}")
    boolean skipTwilio;
    
    @Value(value = "${search.skip.nexmo}")
    boolean skipNexmo;
    
    @Value(value = "${search.skip.plivo}")
    boolean skipPlivo;

    public NumberResponse searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            Provider provider, String countryIsoCode, String numberType,
            String pattern, String nextPlivoIndex, String nextNexmoIndex,
            String markup) throws ClientProtocolException, IOException {
        NumberResponse numberResponse = new NumberResponse();
        String twiloIndex = "";
        if ("FIRST".equalsIgnoreCase(nextNexmoIndex)
                || "FIRST".equalsIgnoreCase(nextPlivoIndex)
                || "1".equalsIgnoreCase(nextNexmoIndex)
                || "0".equalsIgnoreCase(nextPlivoIndex)) {
            twiloIndex = "";
        } else {
            twiloIndex = "INDEXED";
        }
        if (provider.getName().equalsIgnoreCase(PLIVO)) {
            plivoFactoryImpl.searchPhoneNumbers(
                    providerService.getPlivioProvider(), serviceTypeEnum,
                    countryIsoCode, numberType, pattern, nextPlivoIndex,
                    numberResponse, markup);
        } else if (provider.getName().equalsIgnoreCase(TWILIO)) {
            twilioFactoryImpl.searchPhoneNumbers(
                    providerService.getTwilioProvider(), serviceTypeEnum,
                    countryIsoCode, numberType, pattern, twiloIndex,
                    numberResponse, markup);
        } else if (provider.getName().equalsIgnoreCase(NEXMO)) {
            nexmoFactoryImpl.searchPhoneNumbers(
                    providerService.getNexmoProvider(), serviceTypeEnum,
                    countryIsoCode, numberType, pattern, nextNexmoIndex,
                    numberResponse, markup);
        } else {
            String message = "Provider " + provider.getName() + " is invalid";
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION, message);
        }
        return numberResponse;
    }

    public NumberResponse searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern,
            String nextPlivoIndex, String nextNexmoIndex, String markup)
            throws ClientProtocolException, IOException {
        NumberResponse numberResponse = new NumberResponse();
        String twiloIndex = "";
        if ("FIRST".equalsIgnoreCase(nextNexmoIndex)
                || "FIRST".equalsIgnoreCase(nextPlivoIndex)
                || "1".equalsIgnoreCase(nextNexmoIndex)
                || "0".equalsIgnoreCase(nextPlivoIndex)) {
            twiloIndex = "";
        } else {
            twiloIndex = "INDEXED";
        }
        if (!skipPlivo) {
            plivoFactoryImpl.searchPhoneNumbers(
                    providerService.getPlivioProvider(), serviceTypeEnum,
                    countryIsoCode, numberType, pattern, nextPlivoIndex,
                    numberResponse, markup);
        }

        if (!skipTwilio) {
            twilioFactoryImpl.searchPhoneNumbers(
                    providerService.getTwilioProvider(), serviceTypeEnum,
                    countryIsoCode, numberType, pattern, twiloIndex,
                    numberResponse, markup);
        }
        if (!skipNexmo) {
            nexmoFactoryImpl.searchPhoneNumbers(
                    providerService.getNexmoProvider(), serviceTypeEnum,
                    countryIsoCode, numberType, pattern, nextNexmoIndex,
                    numberResponse, markup);
        }
        if (numberResponse != null && numberResponse.getObjects() != null) {
            Collections.sort(numberResponse.getObjects(),
                    new Comparator<Number>() {
                        @Override
                        public int compare(Number n1, Number n2) {
                            if (n1.getMonthlyRentalRate() == null
                                    && n2.getMonthlyRentalRate() == null) {
                                return 0;
                            } else if (n1.getMonthlyRentalRate() == null) {
                                return 1;
                            } else if (n2.getMonthlyRentalRate() == null) {
                                return 2;
                            } else {
                                float f1 = Float.parseFloat(n1
                                        .getMonthlyRentalRate());
                                float f2 = Float.parseFloat(n2
                                        .getMonthlyRentalRate());
                                int result = Float.compare(f1, f2);
                                return result;
                            }

                        }
                    });
        }
        return numberResponse;
    }

}