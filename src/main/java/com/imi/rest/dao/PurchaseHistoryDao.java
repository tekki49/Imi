package com.imi.rest.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.Purchase;
import com.imi.rest.dao.model.PurchaseHistory;

@Repository
@Transactional
public class PurchaseHistoryDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public PurchaseHistory getPurhcaseHistory(Integer id) {
        Criteria criteria = getSession().createCriteria(PurchaseHistory.class);
        criteria.add(Restrictions.eq("id", id));
        List<PurchaseHistory> purchaseList = criteria.list();
        return purchaseList.get(0);
    }

    @SuppressWarnings("unchecked")
    public PurchaseHistory getPurchaseHistoryByNumber(String number) {
        PurchaseHistory purchasehistory = null;
        Criteria criteria = getSession().createCriteria(PurchaseHistory.class);
        criteria.add(Restrictions.eq("number", number));
        List<PurchaseHistory> purchaseHistoryList = criteria.list();
        if (purchaseHistoryList != null && purchaseHistoryList.size() > 0) {
            purchasehistory = purchaseHistoryList.get(0);
        }
        return purchasehistory;
    }

    public void createNewPurchaseHistory(PurchaseHistory purchasehistory) {
        getSession().saveOrUpdate(purchasehistory);
    }
}
