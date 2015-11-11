package com.imi.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imi.rest.dao.ForexDao;
import com.imi.rest.dao.model.ForexValues;

@Service
@Transactional
public class ForexService {

    @Autowired
    ForexDao forexDao;

    public ForexValues getForexValueByName(String name) {
        return forexDao.getForexValueByName(name);
    }

}
