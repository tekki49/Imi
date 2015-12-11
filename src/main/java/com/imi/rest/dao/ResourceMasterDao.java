package com.imi.rest.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.imi.rest.dao.model.ResourceMaster;

@Repository
@Transactional
public class ResourceMasterDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void createNewResource(ResourceMaster resourceMaster) {
        getSession().saveOrUpdate(resourceMaster);
    }

    public ResourceMaster getResourceById(int id) {
        Criteria criteria = getSession().createCriteria(ResourceMaster.class);
        criteria.add(Restrictions.eq("resourceId", id));
        List<ResourceMaster> resourceList = criteria.list();
        if (resourceList != null && resourceList.size() > 0) {
            return resourceList.get(0);
        }
        return null;
    }

    public ResourceMaster getResourceByNumber(String number, Integer providerId) {
        Criteria criteria = getSession().createCriteria(ResourceMaster.class);
        criteria.add(Restrictions.eq("serviceCode", number));
        criteria.add(Restrictions.eq("providerId", providerId));
        List<ResourceMaster> resourceList = criteria.list();
        if (resourceList != null && resourceList.size() > 0) {
            return resourceList.get(0);
        }
        return null;
    }

    public void deleteResourceMaster(ResourceMaster resourceMaster) {
        getSession().delete(resourceMaster);
    }

}
