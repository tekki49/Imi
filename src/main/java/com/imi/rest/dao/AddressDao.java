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

import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.UserAddressMgmt;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.UserAddressMgnt;

@Repository
@Transactional
public class AddressDao {

	@Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    
    @SuppressWarnings("unchecked")
    public List<com.imi.rest.model.UserAddressMgnt> getAddressList(String userId, String country) throws ImiException {
        Criteria criteria = getSession().createCriteria(UserAddressMgmt.class);
        Integer id = Integer.parseInt(userId);
        criteria.add(Restrictions.eq("userId", id));
        criteria.add(Restrictions.eq("country", country));
        List<UserAddressMgmt> addressDbList = criteria.list();
        if (addressDbList != null) {
            List<com.imi.rest.model.UserAddressMgnt> addressList = new ArrayList<com.imi.rest.model.UserAddressMgnt>();
            for (UserAddressMgmt daoAddress : addressDbList) {
                UserAddressMgnt userAddressMgntModel = new com.imi.rest.model.UserAddressMgnt();
                userAddressMgntModel.setAddressId(String.valueOf(daoAddress.getId()));
                userAddressMgntModel.setBusinessName(daoAddress.getCompanyName());
                userAddressMgntModel.setBlockName(daoAddress.getAddress());
                userAddressMgntModel.setCity(daoAddress.getCity());
                userAddressMgntModel.setState(daoAddress.getState());
                userAddressMgntModel.setCountry(daoAddress.getCountry());
                userAddressMgntModel.setZipCode(String.valueOf(daoAddress.getPostalCode()) );
                addressList.add(userAddressMgntModel);
            }
            return addressList;
        }
        throw new ImiException("Unable to fetch country data from data base");
    }
}
