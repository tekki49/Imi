package com.imi.rest.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.VoiceRouteMaster;

@Repository
@Transactional
public class VoiceRouteMasterDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void createNewVoiceRouteMaster(VoiceRouteMaster voiceRouteMaster) {
		getSession().saveOrUpdate(voiceRouteMaster);
	}

	public VoiceRouteMaster getResourceById(int providerId, String countryCode, String countryIso) {
		Criteria criteria = getSession().createCriteria(VoiceRouteMaster.class);
		criteria.add(Restrictions.eq("providerId", providerId));
		criteria.add(Restrictions.eq("countryCode", countryCode));
		criteria.add(Restrictions.eq("countryIso", countryIso));
		List<VoiceRouteMaster> voiceRouteMasterList = criteria.list();
		if (voiceRouteMasterList != null && voiceRouteMasterList.size() > 0) {
			return voiceRouteMasterList.get(0);
		}
		return null;
	}

}
