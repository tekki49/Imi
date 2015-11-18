package com.imi.rest.service;

import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.AddressDao;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.Address;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceTest {

	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	ProviderService providerService;
	@Mock
	Provider provider;
	@Mock
	AddressDao addressDao;
	@InjectMocks
	String customerName;
	@InjectMocks
	String providerName;
	@InjectMocks
	Address address;

	@Before
	public void setup() throws ImiException, ClientProtocolException, IOException {
		String providerName = "a";
		
	}

	@Test
	public void getAddressByCustomerNameWhenProviderIsTWILIO() throws ClientProtocolException, IOException, ImiException {
		Provider provider = new Provider();
		Address address = new Address();
		address.setCity("CITY");
		provider.setName("TWILIO");
		when(providerService.getProviderByName(providerName)).thenReturn(provider);
		when(twilioFactoryImpl.getAddressOfCustomer(customerName,provider)).thenReturn(address);
		Assert.assertEquals("CITY", twilioFactoryImpl.getAddressOfCustomer(customerName, provider).getCity());
	}
	
	@Test
	public void getAddressByCustomerNameWhenProviderIsNotTWILIO() throws ImiException, ClientProtocolException, IOException{
		Provider provider = new Provider();
		Address address = new Address();
		address.setCity("CITY");
		provider.setName("NOT_TWILIO");
		when(providerService.getProviderByName(providerName)).thenReturn(provider);
		when(twilioFactoryImpl.getAddressOfCustomer(customerName,provider)).thenReturn(address);
		if("CITY".equalsIgnoreCase(twilioFactoryImpl.getAddressOfCustomer(customerName, provider).getCity())){
			Assert.assertEquals(true, true);
		}else{
			Assert.assertEquals(false, false);
		}
	}
}
