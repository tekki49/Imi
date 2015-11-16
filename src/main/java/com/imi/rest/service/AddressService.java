package com.imi.rest.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.AddressDao;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.UserAddressMgmt;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.Address;
import com.imi.rest.model.Customer;

@Service
public class AddressService implements ProviderConstants {

    @Autowired
    private TwilioFactoryImpl twilioFactoryImpl;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private AddressDao addressDao;

    public Address getAddressByCustomerName(String customerName,
            String providerName)
                    throws ClientProtocolException, IOException, ImiException {
        Provider provider = providerService.getProviderByName(providerName);
        Address address = null;
        if (provider.getName().equalsIgnoreCase(TWILIO)) {
            address = twilioFactoryImpl.getAddressOfCustomer(customerName,
                    provider);
        }
        return address;
    }

    public void updateClientAddressToProvider(Customer customer,
            String providerName)
                    throws ImiException, ClientProtocolException, IOException {
        Provider provider = providerService.getProviderByName(providerName);
        if (providerName.equalsIgnoreCase(TWILIO)) {
            twilioFactoryImpl.createNewAddressOfCustomer(customer, provider);
        }
    }

    public void updateAddress(Customer customer, String clientId) {
        UserAddressMgmt userAddressMgnt = null;
        if (customer.getAddress_id() == null) {
            userAddressMgnt = new UserAddressMgmt();
        } else {
            userAddressMgnt = getUserAddressById(customer.getAddress_id());
        }
        if (userAddressMgnt == null) {
            userAddressMgnt = new UserAddressMgmt();
        }
        userAddressMgnt.setAddress(customer.getStreet());
        userAddressMgnt.setCompanyName(customer.getCustomer());
        userAddressMgnt.setCity(customer.getCity());
        userAddressMgnt.setCountry(customer.getCountry().toUpperCase());
        userAddressMgnt.setState(customer.getState());
        userAddressMgnt
                .setPostalCode(Integer.parseInt(customer.getPostalcode()));
        userAddressMgnt.setUserId(Integer.parseInt(clientId));
        addressDao.createNewAddress(userAddressMgnt);
    }

    private UserAddressMgmt getUserAddressById(Long address_id) {
        return addressDao.getAddressById(address_id);
    }

    public List<Customer> getAddressList(String userId, String country)
            throws ImiException {
        return addressDao.getAddressList(userId,
                country == null ? null : country.toUpperCase());
    }

    public void deleteAddressFromImi(String clientId, String addressId) {
        Long id = Long.valueOf(addressId);
        UserAddressMgmt userAddressMgmt = addressDao.getAddressById(id);
        addressDao.deleteAddress(userAddressMgmt);
    }

}
