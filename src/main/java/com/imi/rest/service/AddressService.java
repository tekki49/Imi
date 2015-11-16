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
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.Address;
import com.imi.rest.model.Customer;
import com.imi.rest.model.UserAddressMgnt;

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

    public void updateClientAddressToProvider(Customer customer, String providerName)
            throws ImiException, ClientProtocolException, IOException {
        Provider provider = providerService.getProviderByName(providerName);
        if (providerName.equalsIgnoreCase(TWILIO)) {
            twilioFactoryImpl.createNewAddressOfCustomer(customer, provider);
        }
    }

    public void updateAddress(Customer customer)
    {
        UserAddressMgnt userAddressMgnt=null;
        if(customer.getAddress_id()==null)
        {
            userAddressMgnt=new UserAddressMgnt();
        }
        userAddressMgnt.setBlockName(customer.getStreet());
        userAddressMgnt.setBusinessName(customer.getCustomer());
        userAddressMgnt.setCity(customer.getCity());
        userAddressMgnt.setCountry(customer.getCountry());
        userAddressMgnt.setState(customer.getState());
        userAddressMgnt.setZipCode(customer.getPostalcode());
        addressDao.createNewAddress(userAddressMgnt);
    }
    
    public List<Customer> getAddressList(String userId, String country)
            throws ImiException {
        return addressDao.getAddressList(userId, country);
    }

}
