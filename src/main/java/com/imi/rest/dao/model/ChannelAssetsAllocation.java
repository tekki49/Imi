package com.imi.rest.dao.model;

// Generated 25 Nov, 2015 2:51:32 PM by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ChannelAssetsAllocation generated by hbm2java
 */
@Entity
@Table(name = "channel_assets_allocation", schema = "ump_identity_mgr")
public class ChannelAssetsAllocation implements java.io.Serializable {

	private Long id;
	private long assetId;
	private Long keywordId;
	private long clientId;
	private Long groupId;
	private Long teamId;
	private Long userId;
	private Integer channelId;
	private Integer assetType;
	private Integer status;
	private Long createdBy;
	private byte isDefault;
	private Byte shareType;
	private Date createdOn;
	private Date updatedOn;

	public ChannelAssetsAllocation() {
	}

	public ChannelAssetsAllocation(long assetId, long clientId, byte isDefault, Date createdOn) {
		this.assetId = assetId;
		this.clientId = clientId;
		this.isDefault = isDefault;
		this.createdOn = createdOn;
	}

	public ChannelAssetsAllocation(long assetId, Long keywordId, long clientId, Long groupId, Long teamId, Long userId,
			Integer channelId, Integer assetType, Integer status, Long createdBy, byte isDefault, Byte shareType,
			Date createdOn, Date updatedOn) {
		this.assetId = assetId;
		this.keywordId = keywordId;
		this.clientId = clientId;
		this.groupId = groupId;
		this.teamId = teamId;
		this.userId = userId;
		this.channelId = channelId;
		this.assetType = assetType;
		this.status = status;
		this.createdBy = createdBy;
		this.isDefault = isDefault;
		this.shareType = shareType;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "asset_id", nullable = false)
	public long getAssetId() {
		return this.assetId;
	}

	public void setAssetId(long assetId) {
		this.assetId = assetId;
	}

	@Column(name = "keyword_id")
	public Long getKeywordId() {
		return this.keywordId;
	}

	public void setKeywordId(Long keywordId) {
		this.keywordId = keywordId;
	}

	@Column(name = "client_id", nullable = false)
	public long getClientId() {
		return this.clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	@Column(name = "group_id")
	public Long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "team_id")
	public Long getTeamId() {
		return this.teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	@Column(name = "user_id")
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "channel_id")
	public Integer getChannelId() {
		return this.channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	@Column(name = "asset_type")
	public Integer getAssetType() {
		return this.assetType;
	}

	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "created_by")
	public Long getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "is_default", nullable = false)
	public byte getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(byte isDefault) {
		this.isDefault = isDefault;
	}

	@Column(name = "share_type")
	public Byte getShareType() {
		return this.shareType;
	}

	public void setShareType(Byte shareType) {
		this.shareType = shareType;
	}

	@Column(name = "created_on", nullable = false, length = 19)
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "updated_on", length = 19)
	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

}
