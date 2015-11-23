package com.imi.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.imi.rest.dao.ProviderDao;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;

public class ProviderServiceTest {


    @Mock
    private ProviderDao dao;
    @Mock
    Provider provider;
    @Mock
    String providerName;

    @Test
	public void getProviderById() throws ImiException {
    	provider=new Provider();
		provider.setId(1);
		Integer providerId=1;
		dao=Mockito.mock(ProviderDao.class);
        doReturn(provider).when(dao).getProvider(providerId);
        assertEquals(Integer.valueOf(1),provider.getId());
    }
    @Test
    public void getProviderByName() throws ImiException {
    	provider=new Provider();
    	provider.setId(1);
		dao=Mockito.mock(ProviderDao.class);
        doReturn(provider).when(dao).getProviderByName("TWILIO");
        assertEquals(Integer.valueOf(1),provider.getId());
    }
    
}
