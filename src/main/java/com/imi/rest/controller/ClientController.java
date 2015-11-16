package com.imi.rest.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.exception.ImiException;
import com.imi.rest.model.ClientRequest;
import com.imi.rest.service.AddressService;
import com.imi.rest.util.ImiJsonUtil;

@RestController
public class ClientController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "/client", method = RequestMethod.POST, consumes = "application/json")
    public String clientListResponse(@RequestBody ClientRequest clientRequest,
            @RequestHeader("provider") String provider)
                    throws ClientProtocolException, ImiException, IOException {
        addressService.updateClientAddress(clientRequest.getClient(), provider);
        return ImiJsonUtil.getJSONString("status", "Client Registered");
    }

}
