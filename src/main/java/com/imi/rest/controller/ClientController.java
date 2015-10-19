package com.imi.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.model.ClientResponse;

@RestController
public class ClientController {

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ClientResponse clientListResponse(
           ) {
        return new ClientResponse();
    }

}
