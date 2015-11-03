package com.imi.rest.dao;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.Country;
import com.imi.rest.exception.ImiException;

@Repository
@Transactional
public class CountryDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public Country getCountryById(Integer id) {
        Criteria criteria = getSession().createCriteria(Country.class);
        criteria.add(Restrictions.eq("id", id));
        List<Country> countryList = criteria.list();
        if (countryList != null && countryList.size() > 0) {
            return countryList.get(0);
        }
        return null;

    }

    @SuppressWarnings("unchecked")
    public Country getCountryByName(String countryName) {
        Criteria criteria = getSession().createCriteria(Country.class);
        criteria.add(Restrictions.eq("country", countryName));
        List<Country> countryList = criteria.list();
        if (countryList != null && countryList.size() > 0) {
            return countryList.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Country getCountryByIso(String countryIso) {
        Criteria criteria = getSession().createCriteria(Country.class);
        criteria.add(Restrictions.eq("countryIso", countryIso));
        List<Country> countryList = criteria.list();
        if (countryList != null && countryList.size() > 0) {
            return countryList.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Set<com.imi.rest.model.Country> getCountrySet() throws ImiException {
        Criteria criteria = getSession().createCriteria(Country.class);
        List<Country> countryList = criteria.list();
        if (countryList != null) {
            Set<com.imi.rest.model.Country> countrySet = new TreeSet<com.imi.rest.model.Country>();
            for (Country daoCountry : countryList) {
                com.imi.rest.model.Country countryModel = new com.imi.rest.model.Country();
                countryModel.setCountry(daoCountry.getCountry());
                countryModel.setIsoCountry(daoCountry.getCountryIso());
                countrySet.add(countryModel);
            }
            return countrySet;
        }
        throw new ImiException("Unable to fetch country data from data base");
    }

    public void batchUpdate(Set<com.imi.rest.model.Country> countryModelSet) {
        for (com.imi.rest.model.Country countryModel : countryModelSet) {
            Country countryDao = getCountryByIso(countryModel.getIsoCountry());
            if (countryDao == null) {
                countryDao = new Country();
                countryDao.setCountry(countryModel.getCountry());
                countryDao.setCountryCode(countryModel.getCountryCode());
                countryDao.setCountryIso(countryModel.getIsoCountry());
                createNewCountry(countryDao);
            }
        }
    }

    private void createNewCountry(Country country) {
        getSession().saveOrUpdate(country);

    }
}
