package com.imi.rest.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.Purchase;

@Repository
@Transactional
public class PurchaseDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public Purchase getPurhcase(Integer purchaseId) {
        Criteria criteria = getSession().createCriteria(Purchase.class);
        criteria.add(Restrictions.eq("id", purchaseId));
        List<Purchase> purchaseList = criteria.list();
        return purchaseList.get(0);
    }

    @SuppressWarnings("unchecked")
    public Purchase getPurchaseByNumber(String number) {
        Criteria criteria = getSession().createCriteria(Purchase.class);
        criteria.add(Restrictions.eq("number", number));
        List<Purchase> purchaseList = criteria.list();
        return purchaseList.get(0);
    }

    public void createNewPurchase(Purchase purchase) {
        getSession().saveOrUpdate(purchase);
    }
    
    public void deletePurchase(Purchase purchase) {
        getSession().delete(purchase);
    }
}
