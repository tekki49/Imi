package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.exception.ImiException;
import com.imi.rest.exception.InvalidProviderException;
import com.imi.rest.model.BalanceResponse;

@Service
public class CheckBalanceService implements ProviderConstants{

    @Autowired
    PlivoFactoryImpl plivioFactoryImpl;

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;

    @Autowired
    NexmoFactoryImpl nexmoFactoryImpl;

    @Autowired
    ProviderService providerService;

    public BalanceResponse checkBalance(String providerName)
                    throws ClientProtocolException, IOException, ImiException {
        BalanceResponse balanceResponse = new BalanceResponse();
        if (providerName.equalsIgnoreCase(PLIVO)) {
            balanceResponse = plivioFactoryImpl.checkBalance(providerService.getPlivioProvider());
        } 
        else if (providerName.equalsIgnoreCase(TWILIO)) {
        	//balanceResponse = twilioFactoryImpl.checkBalance(providerService.getTwilioProvider());
        }
        else if (providerName.equalsIgnoreCase(NEXMO)) {
        	balanceResponse = nexmoFactoryImpl.checkBalance(providerService.getNexmoProvider());
        } else {
            throw new InvalidProviderException(providerName);
        }
        return balanceResponse;
    }
}
