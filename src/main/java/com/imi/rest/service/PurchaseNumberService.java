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
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.PurchaseRequest;
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
            ServiceConstants serviceTypeEnum, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname,
            String clientkey, PurchaseRequest purchaseRequest, String teamuuid)
            throws ClientProtocolException, IOException {
        if (provider.getName().equalsIgnoreCase(TWILIO)) {
            return twilioFactoryImpl.purchaseNumber(number, numberType,
                    provider, country, serviceTypeEnum, userid, clientId,
                    groupid, teamid, clientname, clientkey, purchaseRequest,
                    teamuuid);
        } else if (provider.getName().equalsIgnoreCase(PLIVO)) {
            return plivioFactoryImpl.purchaseNumber(number, numberType,
                    provider, country, serviceTypeEnum, userid, clientId,
                    groupid, teamid, clientname, clientkey, purchaseRequest,
                    teamuuid);
        } else if (provider.getName().equalsIgnoreCase(NEXMO)) {
            return nexmoFactoryImpl.purchaseNumber(number, numberType,
                    provider, country, serviceTypeEnum, userid, clientId,
                    groupid, teamid, clientname, clientkey, purchaseRequest,
                    teamuuid);
        } else if (provider.getName().equalsIgnoreCase(TWILIO_DUMMY)) {
            return twilioFactoryImpl.purchaseNumber(number, numberType,
                    provider, country, serviceTypeEnum, userid, clientId,
                    groupid, teamid, clientname, clientkey, purchaseRequest,
                    teamuuid);
        } else {
            String message="Provider "+provider.getName() +" is invalid";
            throw InboundRestException.createApiException(InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION, message);
        }
    }
}
