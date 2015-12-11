package com.imi.rest.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.UserAddressMgmt;
import com.imi.rest.model.Customer;

@Repository
@Transactional
public class AddressDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public List<com.imi.rest.model.Customer> getAddressList(Integer userId,
            String country) {
        Criteria criteria = getSession().createCriteria(UserAddressMgmt.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("country", country));
        List<UserAddressMgmt> addressDbList = criteria.list();
        List<Customer> customerList = null;
        if (addressDbList != null) {
            customerList = new ArrayList<Customer>();
            for (UserAddressMgmt daoAddress : addressDbList) {
                Customer customer = new Customer();
                customer.setAddress_id(daoAddress.getId());
                customer.setCustomer(daoAddress.getCompanyName());
                customer.setStreet(daoAddress.getAddress());
                customer.setCity(daoAddress.getCity());
                customer.setState(daoAddress.getState());
                customer.setCountry(daoAddress.getCountry());
                customer.setPostalcode(
                        String.valueOf(daoAddress.getPostalCode()));
                customerList.add(customer);
            }
        }
        return customerList;
    }

    public void createNewAddress(UserAddressMgmt addressMgnt) {
        getSession().saveOrUpdate(addressMgnt);
    }

    public UserAddressMgmt getAddressById(Long addressId) {
        UserAddressMgmt userAddressMgmt = null;
        Criteria criteria = getSession().createCriteria(UserAddressMgmt.class);
        criteria.add(Restrictions.idEq(addressId));
        List<UserAddressMgmt> userAddressList = criteria.list();
        if (userAddressList != null && userAddressList.size() > 0)
            userAddressMgmt = userAddressList.get(0);
        return userAddressMgmt;
    }

    public void deleteAddress(UserAddressMgmt userAddressMgmt) {
        getSession().delete(userAddressMgmt);
    }
}
