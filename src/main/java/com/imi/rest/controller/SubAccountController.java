package com.imi.rest.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.SubAccountDetails;
import com.imi.rest.service.ProviderService;
import com.imi.rest.service.SubAccountService;
import com.imi.rest.util.ImiDataFormatUtils;
import com.imi.rest.util.ImiJsonUtil;

@RestController
public class SubAccountController {

    private static final Logger LOG = Logger
            .getLogger(SubAccountController.class);

    @Autowired
    SubAccountService subAccountService;
    @Autowired
    ProviderService providerService;
    @Autowired
    TwilioFactoryImpl twilioFactoryImpl;

    @RequestMapping(value = "/createSubAccount/{friendlyName}/{providerId}", method = RequestMethod.GET)
    public SubAccountDetails getSubAccountDetails(
            @PathVariable("friendlyName") final String friendlyName,
            @PathVariable("providerId") final Integer providerId,
            @RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
            @RequestHeader(value = "userid", defaultValue = "0") Integer userid)
            throws JsonParseException, JsonMappingException, IOException {
        LOG.info("Inside SubAccountController");
        Provider provider = providerService.getProviderById(providerId);
        SubAccountDetails subAccountDetails = subAccountService
                .getSubAccountDetailsByFriendlyName(friendlyName, provider,
                        userid);
        return subAccountDetails;
    }

    @RequestMapping(value = "/create/subAccount/trunksip/{masterTrunkSid}", method = RequestMethod.POST)
    public String createNewSubAccount(
            @PathVariable("masterTrunkSid") final String masterTrunkSid,
            @RequestHeader("provider") final String providerName,
            @RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
            @RequestHeader(value = "userid", defaultValue = "0") Integer userid)
            throws JsonParseException, JsonMappingException, IOException {
        LOG.info("Inside SubAccountController");
        Provider providerObj = null;
        if (ImiDataFormatUtils.isNumber(providerName)) {
            providerObj = providerService.getProviderById(Integer
                    .parseInt(providerName));
        } else {
            providerObj = providerService.getProviderByName(providerName);
        }
        SubAccountDetails subAccountDetails = twilioFactoryImpl
                .createSubAcctDetailsWithTrunk(providerObj, userid, clientId,
                        masterTrunkSid);
        Map<String, String> response = new HashMap<String, String>();
        if (subAccountDetails != null) {
            response.put("status", "OK");
            response.put("code", "0");
            response.put("message", "success");
            response.put("detailedMessage", "Sub account for client" + userid
                    + " created successfully");
            response.put("sid", subAccountDetails.getSid());
        }
        return ImiJsonUtil.serialize(response);
    }

}
