package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.imi.rest.dao.PurchaseDao;
import com.imi.rest.dao.model.Purchase;

public class PurchaseServiceTest {


    @Mock
    PurchaseDao purchaseDao;
    @Mock
    Purchase purchase;
    @Mock
    String number;

    @Test
    public void getPurchaseByNumber() {
    	purchaseDao=Mockito.mock(PurchaseDao.class);
    	number="123456798";
    	purchase =new Purchase();
    	purchase.setId(1);
    	doReturn(purchase).when(purchaseDao).getPurchaseByNumber(number);
    	assertEquals(Integer.valueOf(1), purchase.getId());
    }
}
