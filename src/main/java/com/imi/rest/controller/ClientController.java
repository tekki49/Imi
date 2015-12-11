package com.imi.rest.controller;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.model.Customer;
import com.imi.rest.service.AddressService;
import com.imi.rest.util.ImiJsonUtil;

@RestController
public class ClientController {

    @Autowired
    private AddressService addressService;

    private static final Logger LOG = Logger.getLogger(ClientController.class);

    @RequestMapping(value = "/client/{clientId2}", method = RequestMethod.POST, consumes = "application/json")
    public String updateAdressToProvider(
            @RequestBody Customer clientRequest,
            @PathVariable("clientId2") String clientId2,
            @RequestHeader("provider") String provider,
            @RequestHeader(value = "userid", defaultValue = "0") Integer userid,
            @RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
            @RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
            @RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
            @RequestHeader(value = "clientname", defaultValue = "0") String clientname,
            @RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
            @RequestHeader(value = "teamuuid", defaultValue = "0") String teamuuid)
            throws ClientProtocolException, IOException {
        LOG.info("Inside ClientController");
        addressService.updateClientAddressToProvider(clientRequest, provider,clientId);
        addressService.updateAddress(clientRequest, userid);
        return ImiJsonUtil.getJSONString("status", "Client Registered");
    }

    @RequestMapping(value = "/client/getAll/{clientId2}/{country}", method = RequestMethod.GET)
    public List<Customer> clientListResponse(
            @RequestHeader("provider") String provider,
            @PathVariable("clientId2") final String clientId2,
            @PathVariable("country") final String country,
            @RequestHeader(value = "userid", defaultValue = "0") Integer userid,
            @RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
            @RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
            @RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
            @RequestHeader(value = "clientname", defaultValue = "0") String clientname,
            @RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
            @RequestHeader(value = "teamuuid", defaultValue = "0") String teamuuid)
            throws ClientProtocolException,  IOException {
        LOG.info("Inside ClientController");
        return addressService.getAddressList(userid, country);
    }

    @RequestMapping(value = "/client/save/{clientId2}/{country}", method = RequestMethod.POST, consumes = "application/json")
    public String updateAdressToImi(
            @RequestHeader(value = "provider", required = false) String provider,
            @RequestBody Customer clientRequest,
            @PathVariable("clientId2") final String clientId2,
            @PathVariable("country") final String country,
            @RequestHeader(value = "userid", defaultValue = "0") Integer userid,
            @RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
            @RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
            @RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
            @RequestHeader(value = "clientname", defaultValue = "0") String clientname,
            @RequestHeader(value = "clientkey", defaultValue = "0") String clientkey)
            throws ClientProtocolException,  IOException {
        LOG.info("Inside ClientController");
        //need to change the logic here
        addressService.updateAddress(clientRequest, userid);
        return ImiJsonUtil.getJSONString("status",
                "Client Address Details Updated Successfully");
    }

    @RequestMapping(value = "/client/delete/{clientId2}/{addressId}," + "", method = RequestMethod.DELETE)
    public String clientListResponse(
            @PathVariable("clientId2") final String clientId2,
            @PathVariable("addressId") final String addressId,
            @RequestHeader(value = "userid", defaultValue = "0") Integer userid,
            @RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
            @RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
            @RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
            @RequestHeader(value = "clientname", defaultValue = "0") String clientname,
            @RequestHeader(value = "clientkey", defaultValue = "0") String clientkey)
            throws ClientProtocolException,  IOException {
        LOG.info("Inside ClientController");
        addressService.deleteAddressFromImi(userid, addressId);
        return ImiJsonUtil.getJSONString("status",
                "Client Address Details Deleted Successfully");
    }
}
