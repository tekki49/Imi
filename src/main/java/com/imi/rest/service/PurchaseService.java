package com.imi.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imi.rest.dao.PurchaseDao;
import com.imi.rest.db.model.Purchase;
import com.imi.rest.model.PurchaseResponse;

@Service
@Transactional
public class PurchaseService {

	@Autowired
	PurchaseDao purchaseDao;
	
	public Purchase getPurchaseByNumber(Integer number){
		return purchaseDao.getByNumber(number);
	}
}
