package com.imi.rest.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Purchase;
import com.imi.rest.model.PurchaseRequest;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.CountrySearchService;
import com.imi.rest.service.ProviderService;
import com.imi.rest.service.PurchaseDetailsService;
import com.imi.rest.service.PurchaseNumberService;
import com.imi.rest.util.ImiDataFormatUtils;

@RestController
public class PurchaseController {

	@Autowired
	PurchaseNumberService purchaseNumberService;
	@Autowired
	ProviderService providerService;
	@Autowired
	PurchaseDetailsService purchaseDetailsService;
	@Autowired
	CountrySearchService countrySearchService;

	private static final Logger LOG = Logger.getLogger(PurchaseController.class);

	@RequestMapping(value = "/purchase", method = RequestMethod.POST, consumes = "application/json")
	public PurchaseResponse purchaseNumber(@RequestBody PurchaseRequest purchaseRequest,
			@RequestHeader(value = "userid", defaultValue = "0") Integer userid,
			@RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
			@RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
			@RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
			@RequestHeader(value = "clientname", defaultValue = "0") String clientname,
			@RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
			@RequestHeader(value = "markup", defaultValue = "0") String markup,
			@RequestHeader(value = "teamuuid", defaultValue = "0") String teamuuid)
					throws ClientProtocolException, IOException {
		LOG.info("Inside PurchaseController");
		Provider providerObj = null;
		if (ImiDataFormatUtils.isNumber(purchaseRequest.getProvider())) {
			providerObj = providerService.getProviderById(Integer.parseInt(purchaseRequest.getProvider()));
		} else {
			providerObj = providerService.getProviderByName(
					purchaseRequest.getProvider() == null ? "" : purchaseRequest.getProvider().toUpperCase());
		}
		Country country = countrySearchService.getCountryByIsoCode(purchaseRequest.getCountryIso());
		ServiceConstants serviceTypeEnum = ServiceConstants.evaluate(purchaseRequest.getService());
		purchaseRequest = ImiDataFormatUtils.formatPurchaseRequest(purchaseRequest);
		PurchaseResponse purchaseResponse = purchaseNumberService.purchaseNumber(purchaseRequest.getNumber(),
				purchaseRequest.getNumberType(), providerObj, country, serviceTypeEnum, userid, clientId, groupid,
				teamid, clientname, clientkey, purchaseRequest, teamuuid);
		return purchaseResponse;
	}

	@RequestMapping(value = "/purchaseByNumber", method = RequestMethod.POST)
	public PurchaseResponse puchaseByNumber(@RequestHeader("number") String number) {
		LOG.info("Inside PurchaseController");
		Purchase purchase = purchaseDetailsService.getPurchaseByNumber(number);
		return new PurchaseResponse(purchase);
	}
}