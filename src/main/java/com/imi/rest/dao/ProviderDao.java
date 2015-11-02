package com.imi.rest.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.exception.InvalidProviderException;

@Repository
@Transactional
public class ProviderDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public Provider getProvider(int providerId) throws ImiException {
        Criteria criteria = getSession().createCriteria(Provider.class);
        criteria.add(Restrictions.eq("id", providerId));
        List<Provider> providerList = criteria.list();
        if (providerList == null || providerList.size() == 0) {
            throw new InvalidProviderException(providerId);
        }
        return providerList.get(0);
    }

    @SuppressWarnings("unchecked")
    public Provider getProviderByName(String providerName) throws ImiException {
        Criteria criteria = getSession().createCriteria(Provider.class);
        criteria.add(Restrictions.eq("name", providerName));
        List<Provider> providerList = criteria.list();
        if (providerList == null || providerList.size() == 0)
            throw new InvalidProviderException(providerName);
        return providerList.get(0);
    }
}
