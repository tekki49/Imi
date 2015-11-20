package com.imi.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.dao.ProviderDao;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;

@Service
public class ProviderService implements ProviderConstants {

    @Autowired
    private ProviderDao dao;

    public ProviderDao getDao() {
		return dao;
	}

	public void setDao(ProviderDao dao) {
		this.dao = dao;
	}

	public Provider getProviderById(int providerId) throws ImiException {
        return dao.getProvider(providerId);
    }

    public Provider getProviderByName(String provider) throws ImiException {
        return dao.getProviderByName(provider);
    }

    public Provider getTwilioProvider() throws ImiException {
        return getProviderByName(TWILIO);
    }

    public Provider getPlivioProvider() throws ImiException {
        return getProviderByName(PLIVO);
    }

    public Provider getNexmoProvider() throws ImiException {
        return getProviderByName(NEXMO);
    }
}
