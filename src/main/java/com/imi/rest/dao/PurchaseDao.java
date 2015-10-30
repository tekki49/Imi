package com.imi.rest.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import com.imi.rest.db.model.Purchase;

@Configuration
@Repository
public class PurchaseDao {

	@Autowired
	private SessionFactory sessionFactory;
	 
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
	@SuppressWarnings("unchecked")
	public Purchase getPurhcase(Integer purchaseId){
		Criteria criteria = getSession().createCriteria(Purchase.class);
        criteria.add(Restrictions.eq("id",purchaseId));
        List<Purchase> purchaseList=criteria.list();
        return purchaseList.get(0);
	}
	@SuppressWarnings("unchecked")
	public Purchase getByNumber(Integer number){
		Criteria criteria = getSession().createCriteria(Purchase.class);
        criteria.add(Restrictions.eq("number",number));
        List<Purchase> purchaseList=criteria.list();
        return purchaseList.get(0);
	}
}
