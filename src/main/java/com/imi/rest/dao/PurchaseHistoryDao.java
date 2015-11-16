package com.imi.rest.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.Purchase;
import com.imi.rest.dao.model.Purchasehistory;

@Configuration
@Repository
public class PurchaseHistoryDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public Purchase getPurhcaseHistory(Integer purchaseId) {
        Criteria criteria = getSession().createCriteria(Purchase.class);
        criteria.add(Restrictions.eq("id", purchaseId));
        List<Purchase> purchaseList = criteria.list();
        return purchaseList.get(0);
    }

    @SuppressWarnings("unchecked")
    public Purchasehistory getPurchaseHistoryByNumber(Integer number) {
        Purchasehistory purchasehistory = null;
        Criteria criteria = getSession().createCriteria(Purchasehistory.class);
        criteria.add(Restrictions.eq("number", number));
        List<Purchasehistory> purchaseHistoryList = criteria.list();
        if (purchaseHistoryList != null && purchaseHistoryList.size() > 0) {
            purchasehistory = purchaseHistoryList.get(0);
        }
        return purchasehistory;
    }

    public void createNewPurchaseHistory(Purchasehistory purchasehistory) {
        getSession().saveOrUpdate(purchasehistory);
    }
}
