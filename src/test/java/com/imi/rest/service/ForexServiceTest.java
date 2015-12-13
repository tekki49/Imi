package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.imi.rest.dao.ForexDao;
import com.imi.rest.dao.model.ForexValues;


public class ForexServiceTest {

	@Mock 
	ForexDao forexDao;
	@InjectMocks
	ForexService forexService;
	
	String name;
	ForexValues forexValues;
	
	@Before
	public void setUp(){
		forexValues=new ForexValues();
		forexValues.setName("USD to GBP");
		forexValues.setValue(Double.valueOf(12.00));
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getForexValueByName() {
		doReturn(forexValues).when(forexDao).getForexValueByName(name);
		assertEquals(Double.valueOf(12.00),forexValues.getValue());
	}
}
