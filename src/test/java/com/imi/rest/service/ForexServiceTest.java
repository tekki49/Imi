package com.imi.rest.service;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.imi.rest.dao.ForexDao;
import com.imi.rest.dao.model.ForexValues;

import static org.junit.Assert.*;

public class ForexServiceTest {

	@Mock
	String name;

	@Test
	public void getForexValueByName() {
		ForexDao forexDao = Mockito.mock(ForexDao.class);
		ForexValues forexValues = new ForexValues();
		forexValues.setName("USD to GBP");
		forexValues.setValue(Double.valueOf(12.00));
		Mockito.doReturn(forexValues).when(forexDao).getForexValueByName(name);
		assertEquals(Double.valueOf(12.00), forexValues.getValue());
	}
}
