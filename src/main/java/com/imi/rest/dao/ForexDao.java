package com.imi.rest.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.ForexValues;

@Repository
@Transactional
public class ForexDao {

	@Autowired
	private SessionFactory sessionFactory;
	Transaction tx = null;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public ForexValues getForexValueById(Integer id) {
		Criteria criteria = getSession().createCriteria(ForexValues.class);
		criteria.add(Restrictions.eq("id", id));
		List<ForexValues> forexValueList = criteria.list();
		if (forexValueList != null && forexValueList.size() > 0) {
			return forexValueList.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public ForexValues getForexValueByName(String forexValueName) {
		Criteria criteria = getSession().createCriteria(ForexValues.class);
		criteria.add(Restrictions.eq("name", forexValueName));
		List<ForexValues> forexValueList = criteria.list();
		if (forexValueList != null && forexValueList.size() > 0) {
			return forexValueList.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public void setForexValue(String name, Double value) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(ForexValues.class);
		criteria.add(Restrictions.eq("name", name));
		List<ForexValues> forexValueList = criteria.list();
		tx = session.beginTransaction();
		ForexValues forexValues = new ForexValues(name, value);
		try {
			if (forexValueList.isEmpty()) {
				session.saveOrUpdate(forexValues);
		}} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			tx.commit();
			session.close();
		}
	}

}
