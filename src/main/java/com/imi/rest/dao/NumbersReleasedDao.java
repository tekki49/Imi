package com.imi.rest.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.NumbersReleased;

@Repository
@Transactional
public class NumbersReleasedDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void createNewNumbersReleased(NumbersReleased numbersReleased) {
        getSession().saveOrUpdate(numbersReleased);
    }

}
