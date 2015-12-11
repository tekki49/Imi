package com.imi.rest.service;

import static org.mockito.Mockito.doThrow;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;

public class ReleaseNumberServiceTest {
	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	PlivoFactoryImpl plivioFactoryImpl;
	@Mock
	NexmoFactoryImpl nexmoFactoryImpl;
	@Mock
	String number;
	@Mock
	Provider provider;
	@Mock
	String countryIsoCode;

	@Test
	public void releaseNumberWhenProviderTwilio() throws ClientProtocolException, IOException {
		twilioFactoryImpl = Mockito.mock(TwilioFactoryImpl.class);
		doThrow(new RuntimeException()).when(twilioFactoryImpl).releaseNumber(number, provider, countryIsoCode);
	}

	@Test
	public void releaseNumberWhenProviderPlivo() throws ClientProtocolException, IOException {
		plivioFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		doThrow(new RuntimeException()).when(plivioFactoryImpl).releaseNumber(number, provider, countryIsoCode);
	}

	@Test
	public void releaseNumberWhenProviderNexmo() throws ClientProtocolException, IOException {
		nexmoFactoryImpl = Mockito.mock(NexmoFactoryImpl.class);
		doThrow(new RuntimeException()).when(nexmoFactoryImpl).releaseNumber(number, provider, countryIsoCode);
	}

}
