package com.imi.rest.dao.model;

// Generated 25 Nov, 2015 2:51:32 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ResourceAllocation generated by hbm2java
 */
@Entity
@Table(name = "resource_allocation", schema = "ump_resource_mgr")
public class ResourceAllocation implements java.io.Serializable {

    private int resourceId;
    private Date createdOn;
    private String nodeId;
    private String nodeType;
    private byte status;
    private Date updatedOn;
    private Integer rcId;
    private String userkey;
    private Date expiresOn;
    private Date activatedOn;
    private String createdBy;
    private Integer groupId;
    private Byte isDefault;
    private Date approvedOn;

    public ResourceAllocation() {
    }

    public ResourceAllocation(Date createdOn, String nodeId, String nodeType,
            byte status, Date updatedOn, Integer rcId, String userkey,
            Date expiresOn, Date activatedOn, String createdBy,
            Integer groupId, Byte isDefault, Date approvedOn) {
        this.createdOn = createdOn;
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.status = status;
        this.updatedOn = updatedOn;
        this.rcId = rcId;
        this.userkey = userkey;
        this.expiresOn = expiresOn;
        this.activatedOn = activatedOn;
        this.createdBy = createdBy;
        this.groupId = groupId;
        this.isDefault = isDefault;
        this.approvedOn = approvedOn;
    }

    @Id
    @Column(name = "resource_id", unique = true, nullable = false)
    public int getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", length = 19)
    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "node_id")
    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Column(name = "node_type")
    public String getNodeType() {
        return this.nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Column(name = "status", nullable = false)
    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on", length = 19)
    public Date getUpdatedOn() {
        return this.updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "rc_id")
    public Integer getRcId() {
        return this.rcId;
    }

    public void setRcId(Integer rcId) {
        this.rcId = rcId;
    }

    @Column(name = "userkey", length = 512)
    public String getUserkey() {
        return this.userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_on", length = 19)
    public Date getExpiresOn() {
        return this.expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activated_on", length = 19)
    public Date getActivatedOn() {
        return this.activatedOn;
    }

    public void setActivatedOn(Date activatedOn) {
        this.activatedOn = activatedOn;
    }

    @Column(name = "created_by")
    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "group_id")
    public Integer getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Column(name = "is_default")
    public Byte getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approved_on", length = 19)
    public Date getApprovedOn() {
        return this.approvedOn;
    }

    public void setApprovedOn(Date approvedOn) {
        this.approvedOn = approvedOn;
    }

}
