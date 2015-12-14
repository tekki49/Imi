package com.imi.rest.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.service.ProviderService;
import com.imi.rest.service.ProvisionService;
import com.imi.rest.util.ImiDataFormatUtils;

@RestController
public class ProvisionController {

	@Autowired
	ProvisionService provisionService;
	@Autowired
	ProviderService providerService;

	private static final Logger LOG = Logger.getLogger(ProvisionController.class);

	// Required in case of nexmo
	@RequestMapping(value = "/number/update/{countryIsoCode}/{number}", method = RequestMethod.POST)
	public ApplicationResponse provisionNumber(@PathVariable("countryIsoCode") String countryIsoCode,
			@PathVariable("number") String number, @RequestHeader("provider") String providerName,
			@RequestHeader(value = "userid", defaultValue = "0") Integer userid,
			@RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
			@RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
			@RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
			@RequestHeader(value = "clientname", defaultValue = "0") String clientname,
			@RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
			@RequestHeader(value = "markup", defaultValue = "0") String markup)
					throws ClientProtocolException, IOException {
		LOG.info("Inside ProvisionController");
		Provider providerObj = null;
		if (ImiDataFormatUtils.isNumber(providerName)) {
			providerObj = providerService.getProviderById(Integer.parseInt(providerName));
		} else {
			providerObj = providerService.getProviderByName(providerName == null ? "" : providerName.toUpperCase());
		}
		ApplicationResponse application = new ApplicationResponse();
		ApplicationResponse applicationResponse = provisionService.provisionNumber(number, countryIsoCode, providerObj,
				userid, clientId, groupid, teamid, clientname, clientkey,application);
		return applicationResponse;
	}

	@RequestMapping(value = "/number/update/{number}", method = RequestMethod.POST)
	public ApplicationResponse provisionNumber(@PathVariable("number") String number,
			@RequestHeader("provider") String providerName,
			@RequestHeader(value = "userid", defaultValue = "0") Integer userid,
			@RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
			@RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
			@RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
			@RequestHeader(value = "clientname", defaultValue = "0") String clientname,
			@RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
			@RequestHeader(value = "markup", defaultValue = "0") String markup)
					throws ClientProtocolException, IOException {
		LOG.info("Inside ProvisionController");
		Provider providerObj = null;
		if (ImiDataFormatUtils.isNumber(providerName)) {
			providerObj = providerService.getProviderById(Integer.parseInt(providerName));
		} else {
			providerObj = providerService.getProviderByName(providerName == null ? "" : providerName.toUpperCase());
		}
		ApplicationResponse application = new ApplicationResponse();
		ApplicationResponse applicationResponse = provisionService.provisionNumber(number, "", providerObj, userid,
				clientId, groupid, teamid, clientname, clientkey,application);
		return applicationResponse;
	}

	@RequestMapping(value = "/number/updateAll", method = RequestMethod.POST)
	public ApplicationResponse provisionNumbers(@RequestHeader("provider") String providerName,
			@RequestBody ApplicationResponse application,
			@RequestHeader(value = "userid", defaultValue = "0") Integer userid,
			@RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
			@RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
			@RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
			@RequestHeader(value = "clientname", defaultValue = "0") String clientname,
			@RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
			@RequestHeader(value = "markup", defaultValue = "0") String markup)
					throws ClientProtocolException, IOException {
		LOG.info("Inside ProvisionController");
		ApplicationResponse applicationResponse = provisionService.provisionAllNumbers(providerName, application,
				userid, clientId, groupid, teamid, clientname, clientkey);
		return applicationResponse;
	}

	@RequestMapping(value = "/application/update", method = RequestMethod.POST)
	public ApplicationResponse updateApplication(@RequestHeader("provider") String providerName,
			@RequestBody ApplicationResponse application) throws ClientProtocolException, IOException {
		LOG.info("Inside ProvisionController");
		ApplicationResponse applicationResponse = provisionService.updateApplication(providerName, application);
		return applicationResponse;
	}

	@RequestMapping(value = "/application/create", method = RequestMethod.POST)
	public ApplicationResponse createApplication(@RequestHeader("provider") String providerName,
			@RequestBody ApplicationResponse application) throws ClientProtocolException, IOException {
		LOG.info("Inside ProvisionController");
		ApplicationResponse applicationResponse = provisionService.createApplication(providerName, application);
		return applicationResponse;
	}

	@RequestMapping(value = "/application/get/{appId}", method = RequestMethod.GET)
	public ApplicationResponse getApplication(@PathVariable("appId") String appId,
			@RequestHeader("provider") String providerName) throws ClientProtocolException, IOException {
		LOG.info("Inside ProvisionController");
		ApplicationResponse applicationResponse = provisionService.getApplication(providerName, appId);
		return applicationResponse;
	}
}
