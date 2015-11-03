package com.imi.rest.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.core.ReleaseNumber;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.service.ProviderService;
import com.imi.rest.util.ImiJsonUtil;

@RestController
public class ReleaseNumberController {

    @Autowired
    ReleaseNumber releaseNumberService;

    @Autowired
    ProviderService providerService;

    @RequestMapping(value = "/release/{number}", method = RequestMethod.DELETE)
    public String releaseNumber(@PathVariable("number") String number,
            @PathVariable("countryIsoCode") String countryIsoCode,
            @RequestHeader("provider") String providerName)
                    throws ClientProtocolException, IOException, ImiException {
        Provider provider = providerService.getProviderByName(
                providerName == null ? "" : providerName.toUpperCase());
        // releaseNumberService.releaseNumber(number, provider, countryIsoCode);
        return ImiJsonUtil.getJSONString(number, "released");
    }

}
