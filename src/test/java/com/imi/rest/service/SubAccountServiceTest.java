package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.SubAccountDetails;

public class SubAccountServiceTest {

	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	String friendlyName;
	@Mock
	Provider provider;
	@Mock
	SubAccountDetails subAccountDetails;

	@Test
	public void createNewSubAccount() throws JsonParseException, JsonMappingException, IOException {
		twilioFactoryImpl = Mockito.mock(TwilioFactoryImpl.class);
		subAccountDetails = new SubAccountDetails();
		subAccountDetails.setFriendly_name("FRIENDLY.NAME");
		doReturn(subAccountDetails).when(twilioFactoryImpl).createSubAccount(friendlyName, provider);
		assertEquals("FRIENDLY.NAME", subAccountDetails.getFriendly_name());
	}

	@Test
	public void getSubAccountDetailsByFriendlyName() throws JsonParseException, JsonMappingException, IOException {
		twilioFactoryImpl = Mockito.mock(TwilioFactoryImpl.class);
		subAccountDetails = new SubAccountDetails();
		subAccountDetails.setFriendly_name("FRIENDLY.NAME");
		doReturn(subAccountDetails).when(twilioFactoryImpl).getSubAccountDetails(friendlyName, provider);
		assertEquals("FRIENDLY.NAME", subAccountDetails.getFriendly_name());
	}
}
