package com.imi.rest.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.imi.rest.dao.ProviderDao;
import com.imi.rest.dao.model.Provider;

public class ProviderServiceTest {


    @Mock
    private ProviderDao dao;
    @InjectMocks
    ProviderService providerService;
    
    Provider provider;
    String providerName;
    Integer providerId;

    @Before
    public void setUp(){
    	provider=new Provider();
		provider.setId(1);
		providerId=1;
    	MockitoAnnotations.initMocks(this);
    }
    
    @Test
	public void getProviderById() {
        doReturn(provider).when(dao).getProvider(providerId);
        Provider providerReturnValue=providerService.getProviderById(providerId);
        assertNotNull(providerReturnValue);
        assertEquals(Integer.valueOf(1),providerReturnValue.getId());
    }
    @Test
    public void getProviderByName() {
    	providerName="TWILIO";
        doReturn(provider).when(dao).getProviderByName(providerName);
        Provider providerReturnValue=providerService.getProviderByName(providerName);
        assertEquals(Integer.valueOf(1),providerReturnValue.getId());
    }
    
}
