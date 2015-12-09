package com.imi.rest.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.model.BalanceResponse;
import com.imi.rest.service.CheckBalanceService;

@RestController
public class CheckBalanceController {

    private static final Logger LOG = Logger
            .getLogger(CheckBalanceController.class);

    @Autowired
    CheckBalanceService checkBalanceService;

    @RequestMapping(value = "/check/balance", method = RequestMethod.GET)
    public BalanceResponse getAccountBalance(
            @RequestHeader("provider") String providerName)
            throws ClientProtocolException, IOException {
        LOG.info("Inside check balance controller");
        return checkBalanceService.checkBalance(providerName);
    }
}
