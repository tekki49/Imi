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
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.Address;
import com.imi.rest.model.Customer;
import com.imi.rest.model.SubAccountDetails;

@Service
public class AddressService implements ProviderConstants {

    @Autowired
    private TwilioFactoryImpl twilioFactoryImpl;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private AddressDao addressDao;

    public List<Address> getAddressByCustomerName(String customerName,
            String providerName, Integer clientId)
                    throws ClientProtocolException, IOException {
        Provider provider = providerService.getProviderByName(providerName);
        SubAccountDetails customerAccountDetails = twilioFactoryImpl
                .getSubAccountDetails("" + clientId, provider);
        List<Address> addressList = null;
        if (provider.getName().equalsIgnoreCase(TWILIO)) {
            addressList = twilioFactoryImpl.getAddressOfCustomer(customerName,
                    provider, clientId, customerAccountDetails);
        }
        return addressList;
    }

    public void updateClientAddressToProvider(Customer customer,
            String providerName, Integer clientId)
                    throws ClientProtocolException, IOException {
        Provider provider = providerService.getProviderByName(providerName);
        if (providerName.equalsIgnoreCase(TWILIO)) {
            twilioFactoryImpl.createNewAddressOfCustomer(customer, provider,
                    clientId);
        } else {
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.INVALID_PROVIDER_ACTION_EXCEPTION,
                    "Provider " + providerName
                            + "does not support updating address through api");
        }
    }

    public void updateAddress(Customer customer, int userId) {
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
        userAddressMgnt.setUserId(userId);
        addressDao.createNewAddress(userAddressMgnt);
    }

    private UserAddressMgmt getUserAddressById(Long address_id) {
        return addressDao.getAddressById(address_id);
    }

    public List<Customer> getAddressList(Integer userid, String country) {
        return addressDao.getAddressList(userid,
                country == null ? null : country.toUpperCase());
    }

    public void deleteAddressFromImi(Integer userid, String addressId) {
        Long id = Long.valueOf(addressId);
        UserAddressMgmt userAddressMgmt = addressDao.getAddressById(id);
        addressDao.deleteAddress(userAddressMgmt);
    }

}
