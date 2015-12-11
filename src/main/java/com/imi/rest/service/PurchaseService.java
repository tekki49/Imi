package com.imi.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.dao.PurchaseDao;
import com.imi.rest.dao.model.Purchase;

@Service
public class PurchaseService {

	@Autowired
	PurchaseDao purchaseDao;

	public Purchase getPurchaseByNumber(String number) {
		return purchaseDao.getPurchaseByNumber(number);
	}
}
