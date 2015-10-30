package com.imi.rest.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import com.imi.rest.db.model.Country;

@Configuration
@Repository
public class CountryDao {

	@Autowired
	private SessionFactory sessionFactory;
	 
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
	@SuppressWarnings("unchecked")
	public Country getCountryById(Integer id){
			Criteria criteria = getSession().createCriteria(Country.class);
	        criteria.add(Restrictions.eq("id",id));
	        List<Country> countryList=criteria.list();
	        return countryList.get(0);

	}
	@SuppressWarnings("unchecked")
	public com.imi.rest.db.model.Country getCountryByName(String countryName) {
		Criteria criteria = getSession().createCriteria(Country.class);
        criteria.add(Restrictions.eq("country",countryName));
        List<Country> countryList=criteria.list();
        return countryList.get(0);
	}
	@SuppressWarnings("unchecked")
	public com.imi.rest.db.model.Country getCountryByIso(String countryIsoCode) {
		Criteria criteria = getSession().createCriteria(Country.class);
        criteria.add(Restrictions.eq("countryIsoCode",countryIsoCode));
        List<Country> countryList=criteria.list();
        return countryList.get(0);
	}
}
