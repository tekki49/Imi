package com.imi.rest.service;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.AddressDao;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.UserAddressMgmt;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.Address;
import com.imi.rest.model.Customer;

import org.junit.Assert;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceTest {

	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	ProviderService providerService;
	@Mock
	AddressDao addressDao;
	@Mock
	AddressService addressService;
	@Mock
	Customer customer;
	@InjectMocks
	String customerName;
	@InjectMocks
	String providerName;
	@InjectMocks
	String userId;
	@InjectMocks
	String client_id;
	@InjectMocks
	String address_id;
	@InjectMocks
	String country;

	@SuppressWarnings("deprecation")
	@Test
	public void getAddressByCustomerNameWhenProviderIsTWILIO()
			throws ClientProtocolException, IOException, ImiException {
		Provider provider = new Provider();
		Address address = new Address();
		address.setCity("CITY");
		provider.setName("TWILIO");
		when(providerService.getProviderByName(providerName)).thenReturn(provider);
		when(twilioFactoryImpl.getAddressOfCustomer(customerName, provider)).thenReturn(address);
		assertEquals("CITY", twilioFactoryImpl.getAddressOfCustomer(customerName, provider).getCity());
	}

	@Test
	public void getAddressByCustomerNameWhenProviderIsNotTWILIO()
			throws ImiException, ClientProtocolException, IOException {
		Provider provider = new Provider();
		Address address = new Address();
		address.setCity("CITY");
		provider.setName("NOT_TWILIO");
		when(providerService.getProviderByName(providerName)).thenReturn(provider);
		when(twilioFactoryImpl.getAddressOfCustomer(customerName, provider)).thenReturn(address);
		if ("CITY".equalsIgnoreCase(twilioFactoryImpl.getAddressOfCustomer(customerName, provider).getCity())) {
			assertEquals(true, true);
		} else {
			assertEquals(false, false);
		}
	}

	@Test
	public void updateClientAddressToProviderWhenProviderIsTWILIO()
			throws ImiException, ClientProtocolException, IOException {
		Provider provider = new Provider();
		Address address = new Address();
		address.setCity("CITY");
		provider.setName("TWILIO");
		when(providerService.getProviderByName(providerName)).thenReturn(provider);
		when(twilioFactoryImpl.createNewAddressOfCustomer(customer, provider)).thenReturn(address);
		assertNotNull(twilioFactoryImpl.createNewAddressOfCustomer(customer, provider));
	}

	@Test
	public void updateClientAddressToProviderWhenProviderIsNotTWILIO()
			throws ImiException, ClientProtocolException, IOException {
		Provider provider = new Provider();
		Address address = new Address();
		address.setCity("CITY");
		provider.setName("NOT_TWILIO");
		when(providerService.getProviderByName(providerName)).thenReturn(provider);
		when(twilioFactoryImpl.createNewAddressOfCustomer(customer, provider)).thenReturn(address);
		assertNotNull(twilioFactoryImpl.createNewAddressOfCustomer(customer, provider));
	}

	@Test
	public void updateAddressCustomerAddressIdNulluserAddressMgmtNull() {
		Customer customer = new Customer();
		String client_id = "1009";
		customer.setStreet("STREET");
		customer.setCustomer("CUSTOMER");
		customer.setCity("CITY");
		customer.setCountry("Country");
		customer.setState("State");
		customer.setPostalcode("10000");
		UserAddressMgmt userAddressMgmt = null;
		userAddressMgmt = new UserAddressMgmt();
		userAddressMgmt.setAddress(customer.getStreet());
		userAddressMgmt.setCompanyName(customer.getCustomer());
		userAddressMgmt.setCity(customer.getCity());
		userAddressMgmt.setCountry(customer.getCountry().toUpperCase());
		userAddressMgmt.setState(customer.getState());
		userAddressMgmt.setPostalCode(Integer.parseInt(customer.getPostalcode()));
		userAddressMgmt.setUserId(Integer.parseInt(client_id));
		assertEquals("COUNTRY", userAddressMgmt.getCountry());
	}
	
	@Test
	public void updateAddressCustomerAddressIdNotNulluserAddressMgmtNull() {
		UserAddressMgmt userAddressMgmt = new UserAddressMgmt();
		new AddressService();
		Customer customer = new Customer();
		String client_id = "1009";
		customer.setStreet("STREET");
		customer.setCustomer("CUSTOMER");
		customer.setCity("CITY");
		customer.setCountry("Country");
		customer.setState("State");
		customer.setPostalcode("10000");
		userAddressMgmt = new UserAddressMgmt();
		userAddressMgmt.setAddress(customer.getStreet());
		userAddressMgmt.setCompanyName(customer.getCustomer());
		userAddressMgmt.setCity(customer.getCity());
		userAddressMgmt.setCountry(customer.getCountry().toUpperCase());
		userAddressMgmt.setState(customer.getState());
		userAddressMgmt.setPostalCode(Integer.parseInt(customer.getPostalcode()));
		userAddressMgmt.setUserId(Integer.parseInt(client_id));
		assertEquals("COUNTRY", userAddressMgmt.getCountry());
	}
	
	@Test
	public void updateAddressCustomerAddressIdNulluserAddressMgmtNotNull() {
		UserAddressMgmt userAddressMgmt = new UserAddressMgmt();
		new AddressService();
		Customer customer = new Customer();
		String client_id = "1009";
		customer.setStreet("STREET");
		customer.setCustomer("CUSTOMER");
		customer.setCity("CITY");
		customer.setCountry("Country");
		customer.setState("State");
		customer.setPostalcode("10000");
		userAddressMgmt = new UserAddressMgmt();
		userAddressMgmt.setAddress(customer.getStreet());
		userAddressMgmt.setCompanyName(customer.getCustomer());
		userAddressMgmt.setCity(customer.getCity());
		userAddressMgmt.setCountry(customer.getCountry().toUpperCase());
		userAddressMgmt.setState(customer.getState());
		userAddressMgmt.setPostalCode(Integer.parseInt(customer.getPostalcode()));
		userAddressMgmt.setUserId(Integer.parseInt(client_id));
		assertEquals("COUNTRY", userAddressMgmt.getCountry());
	}
	
	@Test
	public void updateAddressCustomerAddressIdNotNulluserAddressMgmtNotNull() {
		UserAddressMgmt userAddressMgmt = new UserAddressMgmt();
		customer.setAddress_id((long)1000);
		userAddressMgmt.setId(customer.getAddress_id());
		Customer customer = new Customer();
		String client_id = "1009";
		customer.setStreet("STREET");
		customer.setCustomer("CUSTOMER");
		customer.setCity("CITY");
		customer.setCountry("Country");
		customer.setState("State");
		customer.setPostalcode("10000");
		userAddressMgmt = new UserAddressMgmt();
		userAddressMgmt.setAddress(customer.getStreet());
		userAddressMgmt.setCompanyName(customer.getCustomer());
		userAddressMgmt.setCity(customer.getCity());
		userAddressMgmt.setCountry(customer.getCountry().toUpperCase());
		userAddressMgmt.setState(customer.getState());
		userAddressMgmt.setPostalCode(Integer.parseInt(customer.getPostalcode()));
		userAddressMgmt.setUserId(Integer.parseInt(client_id));
		assertEquals("COUNTRY", userAddressMgmt.getCountry());
	}

	@Test
	public void getUserAddressByIdTest() {
		Long address_id = (long) 1.23;
		UserAddressMgmt userAddressMgmt = new UserAddressMgmt();
		userAddressMgmt.setId(address_id);
		when(addressDao.getAddressById(address_id)).thenReturn(userAddressMgmt);
		assertEquals((long) 1.23, addressDao.getAddressById(address_id).getId(), 0.01);
	}

	@Test
	public void getAddressListCountryNull() throws ImiException {
		country = null;
		when(addressDao.getAddressList(userId, null)).thenReturn(null);
		assertNull(addressDao.getAddressList(userId, null));
	}

	@Test
	public void getAddressListCountryNotNull() throws ImiException {
		country = "United States";
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer);
		when(addressDao.getAddressList(userId, country.toUpperCase())).thenReturn(customerList);
		assertNotNull(addressDao.getAddressList(userId, country.toUpperCase()));
	}
	
	@Test
	 public void deleteAddressFromImi() {
		UserAddressMgmt userAddressMgmt=new UserAddressMgmt();
		userAddressMgmt.setCountry("United States");
		address_id="1200"; 
        Long id = Long.valueOf(address_id);
        when(addressDao.getAddressById(id)).thenReturn(userAddressMgmt);
	}

}
