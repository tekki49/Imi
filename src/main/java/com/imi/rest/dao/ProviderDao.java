package com.imi.rest.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import com.imi.rest.db.model.Provider;

@Configuration
@Repository
public class ProviderDao {

	@Autowired
	private SessionFactory sessionFactory;
	 
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
	@SuppressWarnings("unchecked")
	public Provider getProvider(Integer providerId){
		Criteria criteria = getSession().createCriteria(Provider.class);
        criteria.add(Restrictions.eq("id",providerId));
        List<Provider> providerList=criteria.list();
        return providerList.get(0);
	}
}
