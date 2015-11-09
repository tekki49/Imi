package com.imi.rest.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.Provisioning;
import com.imi.rest.exception.ImiException;

@Configuration
@Repository
public class ProvisioningDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void updateProvisioning(Provisioning provisioning) {
        getSession().saveOrUpdate(provisioning);
    }

    public Provisioning getProvisioning(String provisioningId)
            throws ImiException {
        Criteria criteria = getSession().createCriteria(Provisioning.class);
        criteria.add(Restrictions.eq("Id", provisioningId));
        List<Provisioning> provisioningList = criteria.list();
        if (provisioningList == null || provisioningList.size() == 0) {
            throw new ImiException(
                    "provisioningId: " + provisioningId + " is not valid");
        }
        return provisioningList.get(0);
    }
}
