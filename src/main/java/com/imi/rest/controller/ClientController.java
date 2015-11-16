package com.imi.rest.controller;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.exception.ImiException;
import com.imi.rest.model.ClientRequest;
import com.imi.rest.model.Customer;
import com.imi.rest.service.AddressService;
import com.imi.rest.util.ImiJsonUtil;

@RestController
public class ClientController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "/client", method = RequestMethod.POST, consumes = "application/json")
    public String updateAdressToProvider(
            @RequestBody ClientRequest clientRequest,
            @RequestHeader("provider") String provider)
                    throws ClientProtocolException, ImiException, IOException {
        addressService.updateClientAddressToProvider(clientRequest.getClient(),
                provider);
        addressService.updateAddress(clientRequest.getClient());
        return ImiJsonUtil.getJSONString("status", "Client Registered");
    }

    @RequestMapping(value = "/client/getAll/{clientId}/{country}")
    public List<Customer> clientListResponse(
            @RequestHeader("provider") String provider,
            @PathVariable("clientId") final String clientId,
            @PathVariable("country") final String country)
                    throws ClientProtocolException, ImiException, IOException {
        return addressService.getAddressList(clientId, country);
    }

    @RequestMapping(value = "/client/save/{clientId}/{country}", method = RequestMethod.POST, consumes = "application/json")
    public List<Customer> updateAdressToImi(
            @RequestHeader(value = "provider", required = false) String provider,
            @RequestBody ClientRequest clientRequest,
            @PathVariable("clientId") final String clientId,
            @PathVariable("country") final String country)
                    throws ClientProtocolException, ImiException, IOException {
        return addressService.getAddressList(clientId, country);
    }
}
