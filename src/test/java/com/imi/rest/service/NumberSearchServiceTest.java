package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.core.impl.NexmoFactoryImpl;
import com.imi.rest.core.impl.PlivoFactoryImpl;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.Meta;
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
	@InjectMocks
	NumberSearchService numberSearchService;
	
	ServiceConstants serviceTypeEnum;
	NumberResponse numberResponse;
	Provider provider;
	String countryIsoCode, numberType, pattern, nextPlivoIndex, nextNexmoIndex, twilioIndex, markup;
	
	@Before
	public void setUp(){
		serviceTypeEnum = ServiceConstants.SMS;
		countryIsoCode = "US";
		numberType = "MOBILE";
		nextPlivoIndex = "2";
		nextNexmoIndex = "3";
		pattern = "123";
		markup = "GBP";
		provider = new Provider();
		provider.setId(1);
		numberResponse = new NumberResponse();
		numberResponse.setCount(1);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void searchPhoneNumbersProviderPlivo() throws ClientProtocolException, IOException {
		

		provider.setName("PLIVO");
		twilioIndex = "";
			twilioIndex = "INDEXED";
		doNothing().when(plivoFactoryImpl).searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode, numberType,
				pattern, nextPlivoIndex, numberResponse, markup);
		assertEquals(1, numberResponse.getCount());
	}
	@Test
	public void searchPhoneNumbersProviderPlivoWithIndices() throws ClientProtocolException, IOException {
		provider.setName("PLIVO");
		twilioIndex = "";
			twilioIndex = "";
		doNothing().when(plivoFactoryImpl).searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode, numberType,
				pattern, nextPlivoIndex, numberResponse, markup);
		assertEquals(1, numberResponse.getCount());
	}
	@Test
	public void searchPhoneNumbersParamsNotNullWithIndices() throws ClientProtocolException, IOException {
		twilioIndex = "";
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
				numberType, pattern, nextPlivoIndex, numberResponse, markup);
		doNothing().when(twilioFactoryImpl).searchPhoneNumbers(providerTwilio, serviceTypeEnum,
				countryIsoCode, numberType, pattern, twilioIndex, numberResponse, markup);
		doNothing().when(nexmoFactoryImpl).searchPhoneNumbers(providerNexmo, serviceTypeEnum,
				countryIsoCode, numberType, pattern, nextNexmoIndex, numberResponse, markup);
		List<Number> objects =mock(List.class);
		Number number=mock(Number.class);
		Number number1=mock(Number.class);
		Meta meta=mock(Meta.class);
		objects.add(number);
		objects.add(number1);
		numberResponse.setCount(1);
		numberResponse.setObjects(objects);
		numberResponse.setMeta(meta);
		//TODO test the commented part
			/*Collections.sort(numberResponse.getObjects(), new Comparator<Number>() {
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
			});*/
	}
	@Test
	public void searchPhoneNumbersParamsNotNullWithoutIndices() throws ClientProtocolException, IOException {
		numberResponse = new NumberResponse();
		plivoFactoryImpl =  mock(PlivoFactoryImpl.class);
		twilioFactoryImpl =  mock(TwilioFactoryImpl.class);
		nexmoFactoryImpl =  mock(NexmoFactoryImpl.class);
		twilioIndex = "INDEXED";
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
				numberType, pattern, nextPlivoIndex, numberResponse, markup);
		doNothing().when(twilioFactoryImpl).searchPhoneNumbers(providerTwilio, serviceTypeEnum,
				countryIsoCode, numberType, pattern, twilioIndex, numberResponse, markup);
		doNothing().when(nexmoFactoryImpl).searchPhoneNumbers(providerNexmo, serviceTypeEnum,
				countryIsoCode, numberType, pattern, nextNexmoIndex, numberResponse, markup);
		List<Number> objects =mock(List.class);
		Number number=mock(Number.class);
		number.setMonthlyRentalRate("2.35");
		Number number1=mock(Number.class);
		number1.setMonthlyRentalRate("3.56");
		Meta meta=mock(Meta.class);
		objects.add(number);
		objects.add(number1);
		numberResponse.setCount(1);
		numberResponse.setObjects(objects);
		numberResponse.setMeta(meta);
		//TODO test the commented part
			/*Collections.sort(numberResponse.getObjects(), new Comparator<Number>() {
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
			});*/
	}
	@Test
	public void searchPhoneNumbersParamsNullNoIndices() throws ClientProtocolException, IOException {
		numberResponse = new NumberResponse();
		plivoFactoryImpl =  mock(PlivoFactoryImpl.class);
		twilioFactoryImpl =  mock(TwilioFactoryImpl.class);
		nexmoFactoryImpl =  mock(NexmoFactoryImpl.class);
		twilioIndex = "";
			twilioIndex = "";
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
				numberType, pattern, nextPlivoIndex, numberResponse, markup);
		doNothing().when(twilioFactoryImpl).searchPhoneNumbers(providerTwilio, serviceTypeEnum,
				countryIsoCode, numberType, pattern, twilioIndex, numberResponse, markup);
		doNothing().when(nexmoFactoryImpl).searchPhoneNumbers(providerNexmo, serviceTypeEnum,
				countryIsoCode, numberType, pattern, nextNexmoIndex, numberResponse, markup);
	}
	@Test
	public void searchPhoneNumbersParamsNullWithIndices() throws ClientProtocolException, IOException {
		numberResponse = new NumberResponse();
		plivoFactoryImpl =  mock(PlivoFactoryImpl.class);
		twilioFactoryImpl =  mock(TwilioFactoryImpl.class);
		nexmoFactoryImpl =  mock(NexmoFactoryImpl.class);
			twilioIndex = "INDEXED";
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
				numberType, pattern, nextPlivoIndex, numberResponse, markup);
		doNothing().when(twilioFactoryImpl).searchPhoneNumbers(providerTwilio, serviceTypeEnum,
				countryIsoCode, numberType, pattern, twilioIndex, numberResponse, markup);
		doNothing().when(nexmoFactoryImpl).searchPhoneNumbers(providerNexmo, serviceTypeEnum,
				countryIsoCode, numberType, pattern, nextNexmoIndex, numberResponse, markup);
	}
	
}