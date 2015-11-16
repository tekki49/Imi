package com.imi.rest.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.dao.ChannelAssetsAllocationDao;
import com.imi.rest.dao.PurchaseDao;
import com.imi.rest.dao.ResourceAllocationDao;
import com.imi.rest.dao.ResourceMasterDao;
import com.imi.rest.dao.model.ChannelAssetsAllocation;
import com.imi.rest.dao.model.Purchase;
import com.imi.rest.dao.model.ResourceAllocation;
import com.imi.rest.dao.model.ResourceMaster;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.util.ImiDataFormatUtils;

@Service
@Transactional
public class ResourceService {

    @Autowired
    ResourceMasterDao resourceMasterDao;

    @Autowired
    ResourceAllocationDao resourceAllocationDao;

    @Autowired
    ChannelAssetsAllocationDao channelAssetsAllocationDao;

    @Autowired
    PurchaseDao purchaseDao;

    public ResourceMaster updateResource(String number,
            ServiceConstants serviceTypeEnum) {
        ResourceMaster resourceMaster = new ResourceMaster();
        try {
            resourceMaster.setServiceCode(number);
            resourceMaster
                    .setChannel(ImiDataFormatUtils.getChannel(serviceTypeEnum));
            resourceMaster.setResourceType((byte) 1);
            resourceMaster.setCategory((byte) 2);
            resourceMaster.setSubCategory((byte) 0);
            // need to check binddetails and status
            resourceMaster.setCreatedOn(new Date());
            resourceMasterDao.createNewResource(resourceMaster);
        } catch (Exception e) {
        }
        return resourceMaster;
    }

    public void updateResourceAllocation(ResourceMaster resourceMaster) {
        try {
            ResourceAllocation resourceAllocation = new ResourceAllocation();
            resourceAllocation.setResourceId(resourceMaster.getResourceId());
            resourceAllocation.setCreatedOn(new Date());
            // need to confirm if this varies or remains static through out
            resourceAllocation.setNodeId("OH#UMPDEV");
            resourceAllocation.setNodeType("OH");
            resourceAllocation.setStatus((byte) 2);
            resourceAllocation.setUpdatedOn(resourceAllocation.getCreatedOn());
            resourceAllocation.setRcId(null);
            resourceAllocation.setExpiresOn(ImiDataFormatUtils.getmaxDate());
            resourceAllocation
                    .setActivatedOn(resourceAllocation.getCreatedOn());
            resourceAllocation.setCreatedBy("Admin");
            resourceAllocation.setIsDefault((byte) 2);
            resourceAllocationDao
                    .createNewResourceAllocation(resourceAllocation);
        } catch (Exception e) {
        }
    }

    public void updateChannelAssetsAllocation(ResourceMaster resourceMaster) {
        try {
            ChannelAssetsAllocation channelAssetsAllocation = new ChannelAssetsAllocation();
            channelAssetsAllocation.setAssetId(resourceMaster.getResourceId());
            channelAssetsAllocation.setKeywordId(0L);
            channelAssetsAllocation.setChannelId(Integer
                    .parseInt(String.valueOf(resourceMaster.getChannel())));
            channelAssetsAllocation.setIsDefault((byte) 1);
            channelAssetsAllocation.setShareType((byte) 0);
            channelAssetsAllocation.setAssetType(2);
            channelAssetsAllocation.setCreatedOn(new Date());
            channelAssetsAllocation.setUpdatedOn(new Date());
            channelAssetsAllocationDao
                    .createNewChannelAssetsAllocation(channelAssetsAllocation);
        } catch (Exception e) {
        }
    }

    public void updatePurchase(PurchaseResponse purchaseResponse,
            String numberType, String restrictions,
            ResourceMaster resourceMaster) {
        try {
            Purchase purchase = new Purchase();
            purchase.setMonthlyRentalRate(
                    purchaseResponse.getMonthlyRentalRate());
            purchase.setNumberType(numberType);
            purchase.setResouceManagerId(resourceMaster.getResourceId());
            purchase.setEffectiveDate(ImiDataFormatUtils.getCurrentTimeStamp());
            purchase.setRestrictions(restrictions);
            purchase.setSetUpRate(purchaseResponse.getSetUpRate());
            purchase.setSmsRate(purchaseResponse.getSmsRate());
            purchase.setVoicePrice(purchaseResponse.getVoicePrice());
            // purchase.setProvidercountry(providercountry);
            purchaseDao.createNewPurchase(purchase);
        } catch (Exception e) {
        }
    }
}
