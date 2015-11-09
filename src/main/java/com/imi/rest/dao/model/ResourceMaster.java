package com.imi.rest.dao.model;
// Generated 9 Nov, 2015 4:39:35 PM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * ResourceMaster generated by hbm2java
 */
@Entity
@Table(name = "resource_master", catalog = "imi", uniqueConstraints = @UniqueConstraint(columnNames = { "service_code",
		"channel" }) )
public class ResourceMaster implements java.io.Serializable {

	private Integer resourceId;
	private String serviceCode;
	private byte direction;
	private byte channel;
	private String tag;
	private String cost;
	private byte bindDetails;
	private byte status;
	private byte resourceType;
	private byte category;
	private byte subCategory;
	private Date createdOn;
	private Date updatedOn;
	private String createdBy;
	private String profile;
	private String XParams;
	private String operators;
	private String countrys;

	public ResourceMaster() {
	}

	public ResourceMaster(String serviceCode, byte direction, byte channel, byte bindDetails, byte status,
			byte resourceType, byte category, byte subCategory) {
		this.serviceCode = serviceCode;
		this.direction = direction;
		this.channel = channel;
		this.bindDetails = bindDetails;
		this.status = status;
		this.resourceType = resourceType;
		this.category = category;
		this.subCategory = subCategory;
	}

	public ResourceMaster(String serviceCode, byte direction, byte channel, String tag, String cost, byte bindDetails,
			byte status, byte resourceType, byte category, byte subCategory, Date createdOn, Date updatedOn,
			String createdBy, String profile, String XParams, String operators, String countrys) {
		this.serviceCode = serviceCode;
		this.direction = direction;
		this.channel = channel;
		this.tag = tag;
		this.cost = cost;
		this.bindDetails = bindDetails;
		this.status = status;
		this.resourceType = resourceType;
		this.category = category;
		this.subCategory = subCategory;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.createdBy = createdBy;
		this.profile = profile;
		this.XParams = XParams;
		this.operators = operators;
		this.countrys = countrys;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "resource_id", unique = true, nullable = false)
	public Integer getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	@Column(name = "service_code", nullable = false, length = 20)
	public String getServiceCode() {
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	@Column(name = "direction", nullable = false)
	public byte getDirection() {
		return this.direction;
	}

	public void setDirection(byte direction) {
		this.direction = direction;
	}

	@Column(name = "channel", nullable = false)
	public byte getChannel() {
		return this.channel;
	}

	public void setChannel(byte channel) {
		this.channel = channel;
	}

	@Column(name = "tag", length = 80)
	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Column(name = "cost", length = 250)
	public String getCost() {
		return this.cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	@Column(name = "bind_details", nullable = false)
	public byte getBindDetails() {
		return this.bindDetails;
	}

	public void setBindDetails(byte bindDetails) {
		this.bindDetails = bindDetails;
	}

	@Column(name = "status", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Column(name = "resource_type", nullable = false)
	public byte getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(byte resourceType) {
		this.resourceType = resourceType;
	}

	@Column(name = "category", nullable = false)
	public byte getCategory() {
		return this.category;
	}

	public void setCategory(byte category) {
		this.category = category;
	}

	@Column(name = "sub_category", nullable = false)
	public byte getSubCategory() {
		return this.subCategory;
	}

	public void setSubCategory(byte subCategory) {
		this.subCategory = subCategory;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 19)
	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "created_by")
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "profile", length = 65535)
	public String getProfile() {
		return this.profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@Column(name = "x_params", length = 65535)
	public String getXParams() {
		return this.XParams;
	}

	public void setXParams(String XParams) {
		this.XParams = XParams;
	}

	@Column(name = "operators", length = 50)
	public String getOperators() {
		return this.operators;
	}

	public void setOperators(String operators) {
		this.operators = operators;
	}

	@Column(name = "countrys", length = 50)
	public String getCountrys() {
		return this.countrys;
	}

	public void setCountrys(String countrys) {
		this.countrys = countrys;
	}

}
