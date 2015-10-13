package com.imi.rest.db.model;

// Generated Oct 6, 2015 6:50:08 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Provisioning generated by hbm2java
 */
@Entity
@Table(name = "provisioning", catalog = "imi")
public class Provisioning implements java.io.Serializable {

	private int id;
	private String voiceUrl;
	private String voiceMethod;
	private String voiceFallbackUrl;
	private String voiceFallbackMethod;
	private String statusCallBack;
	private String statusCallbackMethod;
	private String smsUrl;
	private String smsMethod;
	private String smsFallbackUrl;
	private String smsFallbackMethod;
	private String smsStatusCallback;
	private Set<Purchasehistories> purchasehistorieses = new HashSet<Purchasehistories>(
			0);

	public Provisioning() {
	}

	public Provisioning(int id) {
		this.id = id;
	}

	public Provisioning(int id, String voiceUrl, String voiceMethod,
			String voiceFallbackUrl, String voiceFallbackMethod,
			String statusCallBack, String statusCallbackMethod, String smsUrl,
			String smsMethod, String smsFallbackUrl, String smsFallbackMethod,
			String smsStatusCallback, Set<Purchasehistories> purchasehistorieses) {
		this.id = id;
		this.voiceUrl = voiceUrl;
		this.voiceMethod = voiceMethod;
		this.voiceFallbackUrl = voiceFallbackUrl;
		this.voiceFallbackMethod = voiceFallbackMethod;
		this.statusCallBack = statusCallBack;
		this.statusCallbackMethod = statusCallbackMethod;
		this.smsUrl = smsUrl;
		this.smsMethod = smsMethod;
		this.smsFallbackUrl = smsFallbackUrl;
		this.smsFallbackMethod = smsFallbackMethod;
		this.smsStatusCallback = smsStatusCallback;
		this.purchasehistorieses = purchasehistorieses;
	}

	@Id
	@Column(name = "Id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "VoiceUrl", length = 45)
	public String getVoiceUrl() {
		return this.voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	@Column(name = "VoiceMethod", length = 45)
	public String getVoiceMethod() {
		return this.voiceMethod;
	}

	public void setVoiceMethod(String voiceMethod) {
		this.voiceMethod = voiceMethod;
	}

	@Column(name = "VoiceFallbackUrl", length = 45)
	public String getVoiceFallbackUrl() {
		return this.voiceFallbackUrl;
	}

	public void setVoiceFallbackUrl(String voiceFallbackUrl) {
		this.voiceFallbackUrl = voiceFallbackUrl;
	}

	@Column(name = "VoiceFallbackMethod", length = 45)
	public String getVoiceFallbackMethod() {
		return this.voiceFallbackMethod;
	}

	public void setVoiceFallbackMethod(String voiceFallbackMethod) {
		this.voiceFallbackMethod = voiceFallbackMethod;
	}

	@Column(name = "StatusCallBack", length = 45)
	public String getStatusCallBack() {
		return this.statusCallBack;
	}

	public void setStatusCallBack(String statusCallBack) {
		this.statusCallBack = statusCallBack;
	}

	@Column(name = "StatusCallbackMethod", length = 45)
	public String getStatusCallbackMethod() {
		return this.statusCallbackMethod;
	}

	public void setStatusCallbackMethod(String statusCallbackMethod) {
		this.statusCallbackMethod = statusCallbackMethod;
	}

	@Column(name = "SmsUrl", length = 45)
	public String getSmsUrl() {
		return this.smsUrl;
	}

	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}

	@Column(name = "SmsMethod", length = 45)
	public String getSmsMethod() {
		return this.smsMethod;
	}

	public void setSmsMethod(String smsMethod) {
		this.smsMethod = smsMethod;
	}

	@Column(name = "SmsFallbackUrl", length = 45)
	public String getSmsFallbackUrl() {
		return this.smsFallbackUrl;
	}

	public void setSmsFallbackUrl(String smsFallbackUrl) {
		this.smsFallbackUrl = smsFallbackUrl;
	}

	@Column(name = "SmsFallbackMethod", length = 45)
	public String getSmsFallbackMethod() {
		return this.smsFallbackMethod;
	}

	public void setSmsFallbackMethod(String smsFallbackMethod) {
		this.smsFallbackMethod = smsFallbackMethod;
	}

	@Column(name = "SmsStatusCallback", length = 45)
	public String getSmsStatusCallback() {
		return this.smsStatusCallback;
	}

	public void setSmsStatusCallback(String smsStatusCallback) {
		this.smsStatusCallback = smsStatusCallback;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "provisioning")
	public Set<Purchasehistories> getPurchasehistorieses() {
		return this.purchasehistorieses;
	}

	public void setPurchasehistorieses(
			Set<Purchasehistories> purchasehistorieses) {
		this.purchasehistorieses = purchasehistorieses;
	}

}