package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.SubAccountDetails;

public class SubAccountServiceTest {


	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@InjectMocks
	SubAccountService subAccountService;
	
	String friendlyName;
	Provider provider;
	SubAccountDetails subAccountDetails;
	Integer clientId;
	
	@Before
	public void setUp(){
		friendlyName = "FRIENDLY.NAME";
		subAccountDetails=new SubAccountDetails();
		subAccountDetails.setFriendly_name(friendlyName);
		provider = new Provider();
		clientId = 123;
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void createNewSubAccount() throws JsonParseException, JsonMappingException, IOException {
		doReturn(subAccountDetails).when(twilioFactoryImpl).createSubAccount("" + clientId, provider);
		SubAccountDetails returnedsubAccDets = subAccountService.createNewSubAccount(friendlyName, provider, clientId);
		assertEquals("FRIENDLY.NAME",returnedsubAccDets.getFriendly_name());
	}
	@Test
	public void getSubAccountDetailsByFriendlyName() throws JsonParseException, JsonMappingException, IOException {
		doReturn(subAccountDetails).when(twilioFactoryImpl).getSubAccountDetails("" + clientId, provider);
		SubAccountDetails returnedsubAccDets = subAccountService.getSubAccountDetailsByFriendlyName(friendlyName, provider, clientId);
		assertEquals("FRIENDLY.NAME",returnedsubAccDets.getFriendly_name());
	}
}
