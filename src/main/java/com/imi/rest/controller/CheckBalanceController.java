package com.imi.rest.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imi.rest.util.ImiJsonUtil;

@RestController
public class CheckBalanceController {

    @RequestMapping(value = "/check/balance", method = RequestMethod.GET)
    public String getAccountBalance(@RequestHeader("provider") String providerId)
            throws JsonProcessingException {
        return ImiJsonUtil.getJSONString("accountBalance", "value");
    }

}
