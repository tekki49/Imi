package com.imi.rest.service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;

import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.AddressDao;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.UserAddressMgmt;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.Address;
import com.imi.rest.model.Customer;
import com.imi.rest.model.SubAccountDetails;

public class AddressServiceTest {

	@Mock
	TwilioFactoryImpl twilioFactoryImpl;
	@Mock
	ProviderService providerService;
	@Mock
	AddressDao addressDao;
	@InjectMocks
	AddressService addressService;

	SubAccountDetails customerAccountDetails;
	Customer customer;
	List<Address> addressList;
	String customerName;
	String providerName;
	Integer userId;
	Integer clientId;
	String addressId;
	String country;
	Address address;
	Provider provider;
	@Mock
	UserAddressMgmt userAddressMgmt;

	@Before
	public void setUp() throws ClientProtocolException, IOException {
		customer = new Customer();
		customer.setStreet("STREET");
		customer.setCustomer("CUSTOMER");
		customer.setCity("CITY");
		customer.setCountry("Country");
		customer.setState("State");
		customer.setPostalcode("10000");
		address=new Address();
		address.setCity("CITY");
		addressList=new ArrayList<Address>();
		addressList.add(address);
		customerName = "CUSTOMER_NAME";
		providerName = "TWILIO";
		userId = 123;
		clientId = 123;
		addressId = "ADDRESS_ID";
		country = "COUNTRY";
		userAddressMgmt = new UserAddressMgmt();
		customerAccountDetails = new SubAccountDetails();
		customerAccountDetails.setFriendly_name("FRIENDLY_NAME");
		provider=new Provider();
		provider.setName(providerName);
		providerService=mock(ProviderService.class);
		twilioFactoryImpl=mock(TwilioFactoryImpl.class);
		MockitoAnnotations.initMocks(this);

		when(providerService.getProviderByName(providerName)).thenReturn(provider);
		when(twilioFactoryImpl.getSubAccountDetails(""+clientId, provider)).thenReturn(customerAccountDetails);		
	}

	@Test
	public void getAddressByCustomerNameWhenProviderIsTWILIO() throws ClientProtocolException, IOException {
		when(twilioFactoryImpl.getAddressOfCustomer(customerName, provider, userId, customerAccountDetails))
		.thenReturn(addressList);
		//addressService.setProviderService(providerService);
		List<Address> returnedAddressListValue = addressService.getAddressByCustomerName(customerName, providerName, userId);
		assertNotNull(returnedAddressListValue );
		assertEquals(returnedAddressListValue.size(), 1);
		Address returnedAddressValue = returnedAddressListValue.get(0);
		assertEquals("CITY", returnedAddressValue.getCity());
		assertEquals(null, returnedAddressValue.getCustomer_name());
	}

	@Test
	public void getAddressByCustomerNameWhenProviderIsNotTWILIO() throws ClientProtocolException, IOException {
		providerName = "NOT_TWILIO";
		provider.setName(providerName);
		when(providerService.getProviderByName(providerName)).thenReturn(provider);
		List<Address> returnedAddressListValue = addressService.getAddressByCustomerName(customerName, providerName, userId);
		assertNull(returnedAddressListValue );
	}
	
	@Test
	public void updateClientAddressToProviderWhenProviderIsTWILIO() throws ClientProtocolException, IOException {
		when(twilioFactoryImpl.createNewAddressOfCustomer(customer, provider, clientId)).thenReturn(address);
		MockitoException e = new MockitoException("exception thrown in updateClientAddressToProviderWhenProviderIsTWILIO");
		try{
			doThrow(e).when(twilioFactoryImpl).createNewAddressOfCustomer(customer, provider, clientId);
			addressService.updateClientAddressToProvider(customer, providerName, clientId);
		}
		catch(MockitoException ex){
			assertEquals("exception thrown in updateClientAddressToProviderWhenProviderIsTWILIO", ex.getMessage());
		}
		assertNotNull(address );
		assertEquals("CITY", address.getCity());
		assertEquals(null, address.getCustomer_name());
	}

	@Test
	public void updateClientAddressToProviderWhenProviderIsNotTWILIO() throws  ClientProtocolException, IOException {
		providerName = "NOT_TWILIO";
		when(twilioFactoryImpl.createNewAddressOfCustomer(customer, provider, clientId)).thenReturn(address);
		try{
			addressService.updateClientAddressToProvider(customer, providerName, clientId);
		}
		catch(InboundRestException ex){
			assertEquals("Provider NOT_TWILIOdoes not support updating address through api", ex.getDetailedMessage());
			assertEquals(InboundApiErrorCodes.INVALID_PROVIDER_ACTION_EXCEPTION.getCode(), ex.getCode());
		}
		assertNotNull(address );
		assertEquals("CITY", address.getCity());
		assertEquals(null, address.getCustomer_name());
	}
	
	@Test
	public void updateAddressCustomerAddressIdNulluserAddressMgmtNull() {
		MockitoException e = new MockitoException("exception thrown in updateAddressCustomerAddressIdNulluserAddressMgmtNull");
		try{
			doThrow(e).when(addressDao).createNewAddress(userAddressMgmt);
			addressService.updateAddress(customer, userId);
		}
		catch(MockitoException ex){
			assertEquals("exception thrown in updateAddressCustomerAddressIdNulluserAddressMgmtNull", ex.getMessage());
		}
		assertNull(customer.getAddress_id());
	}
	@Test
	public void getAddressListTest() {
		List<Customer> customerList = new ArrayList();
		customerList.add(customer);
		when(addressDao.getAddressList(userId, country)).thenReturn(customerList);
		List<Customer> customers = addressService.getAddressList(userId, country);
		assertNotNull(customers);
		assertEquals(customers.size(), 1);
		Customer customer = customers.get(0);
		assertEquals("CUSTOMER", customer.getCustomer());
		assertEquals("CITY", customer.getCity());
		assertEquals("Country", customer.getCountry());
	}
	@Test
	public void deleteAddressFromImiTest() {
		UserAddressMgmt userAddressMgmt = new UserAddressMgmt();
		userAddressMgmt.setCountry("United States");
		addressId = "123";
		Long id = Long.valueOf(addressId);
		when(addressDao.getAddressById(id)).thenReturn(null);
		MockitoException e = new MockitoException("exception thrown in deleteAddressFromImiTest");
		try{
			doThrow(e).when(addressDao).deleteAddress(null);
			addressService.deleteAddressFromImi(userId, addressId);
		}
		catch(MockitoException ex){
			assertEquals("exception thrown in deleteAddressFromImiTest", ex.getMessage());
		}
	}
	

	/*@Test
	public void getAddressByCustomerNameWhenProviderIsNotTWILIO() throws ClientProtocolException, IOException {
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
	public void updateClientAddressToProviderWhenProviderIsTWILIO() throws ClientProtocolException, IOException {
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
			throws , ClientProtocolException, IOException {
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
		customer.setAddress_id((long) 1000);
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
	public void getAddressListCountryNull() {
		country = null;
		when(addressDao.getAddressList(userId, null)).thenReturn(null);
		assertNull(addressDao.getAddressList(userId, null));
	}

	@Test
	public void getAddressListCountryNotNull() {
		country = "United States";
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.add(customer);
		when(addressDao.getAddressList(userId, country.toUpperCase())).thenReturn(customerList);
		assertNotNull(addressDao.getAddressList(userId, country.toUpperCase()));
	}

	@Test
	public void deleteAddressFromImi() {
		UserAddressMgmt userAddressMgmt = new UserAddressMgmt();
		userAddressMgmt.setCountry("United States");
		address_id = "1200";
		Long id = Long.valueOf(address_id);
		when(addressDao.getAddressById(id)).thenReturn(userAddressMgmt);
	}*/

}
