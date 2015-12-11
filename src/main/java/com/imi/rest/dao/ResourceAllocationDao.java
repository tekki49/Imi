package com.imi.rest.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.ResourceAllocation;

@Repository
@Transactional
public class ResourceAllocationDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void createNewResourceAllocation(ResourceAllocation resourceAllocation) {
		getSession().saveOrUpdate(resourceAllocation);
	}

	public ResourceAllocation getResourceAllocationById(int id) {
		Criteria criteria = getSession().createCriteria(ResourceAllocation.class);
		criteria.add(Restrictions.eq("resourceId", id));
		List<ResourceAllocation> resourceList = criteria.list();
		if (resourceList != null && resourceList.size() > 0) {
			return resourceList.get(0);
		}
		return null;
	}

	public void deleteResourceAllocation(ResourceAllocation resourceAllocation) {
		getSession().delete(resourceAllocation);
	}

}
