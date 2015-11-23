package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;

public class NumberSearchServiceTest {

	@Mock
	PlivoFactoryImpl plivoFactoryImpl;
	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	NexmoFactoryImpl nexmoFactoryImpl;
	@Mock
	ProviderService providerService;
	@Mock
	ServiceConstants serviceTypeEnum;
	@Mock
	NumberResponse numberResponse;
	@Mock
	Provider provider;
	@Mock
	String countryIsoCode, numberType, pattern, nextPlivoIndex, nextNexmoIndex, twiloIndex;

	@Test
	public void searchPhoneNumbersProviderPlivo() throws ClientProtocolException, IOException, ImiException {
		serviceTypeEnum = ServiceConstants.SMS;
		countryIsoCode = "US";
		numberType = "MOBILE";
		nextPlivoIndex = "2";
		nextNexmoIndex = "3";
		pattern = "123";
		provider = new Provider();
		provider.setId(1);
		provider.setName("PLIVO");
		numberResponse = new NumberResponse();
		numberResponse.setCount(1);
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		twiloIndex = "";
		if ("FIRST".equalsIgnoreCase(nextNexmoIndex) || "FIRST".equalsIgnoreCase(nextPlivoIndex)
				|| "1".equalsIgnoreCase(nextNexmoIndex) || "0".equalsIgnoreCase(nextPlivoIndex)) {
			twiloIndex = "";
		} else {
			twiloIndex = "INDEXED";
		}
		doNothing().when(plivoFactoryImpl).searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode, numberType,
				pattern, nextPlivoIndex, numberResponse);
		assertEquals(1, numberResponse.getCount());
	}
	@Test
	public void searchPhoneNumbers() throws ClientProtocolException, IOException, ImiException {
		numberResponse = new NumberResponse();
		plivoFactoryImpl = Mockito.mock(PlivoFactoryImpl.class);
		twilioFactoryImpl = Mockito.mock(TwilioFactoryImpl.class);
		nexmoFactoryImpl = Mockito.mock(NexmoFactoryImpl.class);
		twiloIndex = "";
		if ("FIRST".equalsIgnoreCase(nextNexmoIndex) || "FIRST".equalsIgnoreCase(nextPlivoIndex)
				|| "1".equalsIgnoreCase(nextNexmoIndex) || "0".equalsIgnoreCase(nextPlivoIndex)) {
			twiloIndex = "";
		} else {
			twiloIndex = "INDEXED";
		}
		Provider providerPlivo = new Provider();
		providerPlivo.setName("PLIVO");
		Provider providerTwilio = new Provider();
		providerTwilio.setName("TWILIO");
		Provider providerNexmo = new Provider();
		providerNexmo.setName("NEXMO");
		pattern = "123";
		nextPlivoIndex = "2";
		nextNexmoIndex = "3";
		doNothing().when(plivoFactoryImpl).searchPhoneNumbers(providerPlivo, serviceTypeEnum, countryIsoCode,
				numberType, pattern, nextPlivoIndex, numberResponse);
		doNothing().when(twilioFactoryImpl).searchPhoneNumbers(providerTwilio, serviceTypeEnum,
				countryIsoCode, numberType, pattern, twiloIndex, numberResponse);
		doNothing().when(nexmoFactoryImpl).searchPhoneNumbers(providerNexmo, serviceTypeEnum,
				countryIsoCode, numberType, pattern, nextNexmoIndex, numberResponse);
		if (numberResponse != null && numberResponse.getObjects() != null) {
			Collections.sort(numberResponse.getObjects(), new Comparator<Number>() {
				@Override
				public int compare(Number n1, Number n2) {
					if (n1.getMonthlyRentalRate() == null && n2.getMonthlyRentalRate() == null) {
						return 0;
					} else if (n1.getMonthlyRentalRate() == null) {
						return 1;
					} else if (n2.getMonthlyRentalRate() == null) {
						return 2;
					} else {
						float f1 = Float.parseFloat(n1.getMonthlyRentalRate());
						float f2 = Float.parseFloat(n2.getMonthlyRentalRate());
						int result = Float.compare(f1, f2);
						return result;
					}

				}
			});
		}
	}
}