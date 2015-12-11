package com.imi.rest.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class ImiDefaultQueryDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Object> getNamedQueryResults(String namedQuery) {
        SQLQuery query = getSession().createSQLQuery(namedQuery);
        return query.list();
    }
}
