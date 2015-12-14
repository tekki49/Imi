package com.imi.rest.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.imi.rest.dao.PurchaseDao;
import com.imi.rest.dao.model.Purchase;

public class PurchaseDetailsServiceTest {
	
	@Mock
    PurchaseDao purchaseDao;
	@InjectMocks
	PurchaseDetailsService purchaseDetailsService;

	Purchase purchase;
	String number;
	
	@Before
	public void setUp(){
		purchase=new Purchase();
		number="NUMBER";
		purchase.setNumber(number);
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	 public void getPurchaseByNumber() {
        when(purchaseDao.getPurchaseByNumber(number)).thenReturn(purchase);
        Purchase purchaseReturnValue=purchaseDetailsService.getPurchaseByNumber(number);
        assertNotNull(purchaseReturnValue);
        assertEquals(number, purchaseReturnValue.getNumber());
    }
}
