package com.imi.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.dao.ForexDao;
import com.imi.rest.dao.model.ForexValues;

@Service
public class ForexService {

	@Autowired
	ForexDao forexDao;

	public ForexValues getForexValueByName(String name) {
		return forexDao.getForexValueByName(name);
	}

}
