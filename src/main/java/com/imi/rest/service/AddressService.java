package com.imi.rest.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.core.impl.TwilioFactoryImpl;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.Address;
import com.imi.rest.model.Customer;

@Service
public class AddressService implements ProviderConstants {

    @Autowired
    private TwilioFactoryImpl twilioFactoryImpl;

    @Autowired
    private ProviderService providerService;

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

    public void updateClientAddress(Customer customer, String providerName)
            throws ImiException, ClientProtocolException, IOException {
        Provider provider = providerService.getProviderByName(providerName);
        twilioFactoryImpl.createNewAddressOfCustomer(customer, provider);
    }

}
