package com.imi.rest.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.ChannelAssetsAllocation;

@Repository
@Transactional
public class ChannelAssetsAllocationDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void createNewChannelAssetsAllocation(ChannelAssetsAllocation channelAssetsAllocation) {
		getSession().saveOrUpdate(channelAssetsAllocation);
	}

	public ChannelAssetsAllocation getChannelAssetsAllocationById(int id) {
		Criteria criteria = getSession().createCriteria(ChannelAssetsAllocation.class);
		criteria.add(Restrictions.eq("id", id));
		List<ChannelAssetsAllocation> resourceList = criteria.list();
		if (resourceList != null && resourceList.size() > 0) {
			return resourceList.get(0);
		}
		return null;
	}

	public ChannelAssetsAllocation getChannelAssetsAllocationByAssetId(long assetId) {
		Criteria criteria = getSession().createCriteria(ChannelAssetsAllocation.class);
		criteria.add(Restrictions.eq("assetId", assetId));
		List<ChannelAssetsAllocation> resourceList = criteria.list();
		if (resourceList != null && resourceList.size() > 0) {
			return resourceList.get(0);
		}
		return null;
	}

	public void deleteChannelAssetsAllocation(ChannelAssetsAllocation channelAssetsAllocation) {
		getSession().delete(channelAssetsAllocation);
	}
}
