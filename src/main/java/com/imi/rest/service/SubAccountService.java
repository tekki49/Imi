package com.imi.rest.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;

import com.imi.rest.model.SubAccountDetails;

@Service
public class SubAccountService {

    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;

    public SubAccountDetails createNewSubAccount(String friendlyName,
            Provider provider,Integer clientId) throws JsonParseException, JsonMappingException,
            IOException {
        return twilioFactoryImpl.createSubAccount(""+clientId, provider);
    }

    public SubAccountDetails getSubAccountDetailsByFriendlyName(
            String friendlyName, Provider provider,Integer clientId) throws JsonParseException,
            JsonMappingException, IOException {
        return twilioFactoryImpl.getSubAccountDetails(""+clientId, provider);
    }

}
