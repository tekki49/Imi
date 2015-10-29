package com.imi.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.dao.ProviderDao;
import com.imi.rest.db.model.Provider;

@Service
@Transactional
public class ProviderService implements ProviderConstants {

	@Autowired
	private ProviderDao dao;
	
    public String getProviderById(String providerId) {
        String provider = "";
        switch (providerId.trim()) {
        case "1":
            provider = TWILIO;
            break;
        case "2":
            provider = PLIVIO;
            break;
        case "3":
            provider = NEXMO;
            break;
        default:
            break;
        }

        return provider;
    }
    public Provider getProvider(Integer providerId){
    			return dao.getProvider(providerId);
    	
    }
}
