package com.imi.rest.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imi.rest.dao.model.Provider;
import com.imi.rest.service.ProviderService;
import com.imi.rest.service.ReleaseNumberService;
import com.imi.rest.util.ImiDataFormatUtils;
import com.imi.rest.util.ImiJsonUtil;

@RestController
public class ReleaseNumberController {

	@Autowired
	ReleaseNumberService releaseNumberService;

	@Autowired
	ProviderService providerService;
	private static final Logger LOG = Logger.getLogger(ReleaseNumberController.class);

	@RequestMapping(value = "/release/{number}/{countryIsoCode}", method = RequestMethod.DELETE)
	public String releaseNumber(@PathVariable("number") String number,
			@PathVariable("countryIsoCode") String countryIsoCode, @RequestHeader("provider") String providerName,
			@RequestHeader(value = "userid", defaultValue = "0") Integer userid,
			@RequestHeader(value = "clientid", defaultValue = "0") Integer clientId,
			@RequestHeader(value = "groupid", defaultValue = "0") Integer groupid,
			@RequestHeader(value = "teamid", defaultValue = "0") Integer teamid,
			@RequestHeader(value = "clientname", defaultValue = "0") String clientname,
			@RequestHeader(value = "clientkey", defaultValue = "0") String clientkey,
			@RequestHeader(value = "markup", defaultValue = "0") String markup)
					throws ClientProtocolException, IOException {
		LOG.info("Inside ReleaseNumberController");
		Provider providerObj = null;
		if (ImiDataFormatUtils.isNumber(providerName)) {
			providerObj = providerService.getProviderById(Integer.parseInt(providerName));
		} else {
			providerObj = providerService.getProviderByName(providerName == null ? "" : providerName.toUpperCase());
		}
		releaseNumberService.releaseNumber(number, providerObj, countryIsoCode, userid, clientId, groupid, teamid,
				clientname, clientkey);
		return ImiJsonUtil.getJSONString(number, "released");
	}

}
