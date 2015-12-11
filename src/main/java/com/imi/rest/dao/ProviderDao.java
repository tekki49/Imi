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

@Repository
@Transactional
public class ProviderDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public Provider getProvider(int providerId) {
        Provider provider = null;
        Criteria criteria = getSession().createCriteria(Provider.class);
        criteria.add(Restrictions.eq("id", providerId));
        List<Provider> providerList = criteria.list();
        if (providerList != null && providerList.size()> 0) {
            provider = providerList.get(0);
        }
        return provider;
    }

    @SuppressWarnings("unchecked")
    public Provider getProviderByName(String providerName) {
        Provider provider = null;
        Criteria criteria = getSession().createCriteria(Provider.class);
        criteria.add(Restrictions.eq("name", providerName));
        List<Provider> providerList = criteria.list();
        if (providerList != null && providerList.size()>0) {
            provider = providerList.get(0);
        }
        return provider;
    }
}
