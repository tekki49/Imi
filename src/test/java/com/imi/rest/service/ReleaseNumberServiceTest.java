package com.imi.rest.service;

import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;

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
	public void releaseNumberWhenProviderTwilio() throws ClientProtocolException, IOException, ImiException {
		 twilioFactoryImpl= Mockito.mock(TwilioFactoryImpl.class);
		doThrow(new RuntimeException()).when(twilioFactoryImpl).releaseNumber(number, provider, countryIsoCode);
	}

	@Test
	public void releaseNumberWhenProviderPlivo() throws ClientProtocolException, IOException, ImiException {
		plivioFactoryImpl= Mockito.mock(PlivoFactoryImpl.class);
		doThrow(new RuntimeException()).when(plivioFactoryImpl).releaseNumber(number, provider, countryIsoCode);
	}

	@Test
	public void releaseNumberWhenProviderNexmo() throws ClientProtocolException, IOException, ImiException {
		nexmoFactoryImpl= Mockito.mock(NexmoFactoryImpl.class);		
		doThrow(new RuntimeException()).when(nexmoFactoryImpl).releaseNumber(number, provider, countryIsoCode);
	}

}
