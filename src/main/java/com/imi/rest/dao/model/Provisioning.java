package com.imi.rest.dao.model;
// Generated 9 Nov, 2015 4:39:35 PM by Hibernate Tools 4.3.1.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Provisioning generated by hbm2java
 */
@Entity
@Table(name = "provisioning", catalog = "imi")
public class Provisioning implements java.io.Serializable {

    private Integer id;
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
    private Set<Purchasehistory> purchasehistories = new HashSet<Purchasehistory>(
            0);

    public Provisioning() {
    }

    public Provisioning(String voiceUrl, String voiceMethod,
            String voiceFallbackUrl, String voiceFallbackMethod,
            String statusCallBack, String statusCallbackMethod, String smsUrl,
            String smsMethod, String smsFallbackUrl, String smsFallbackMethod,
            String smsStatusCallback, Set<Purchasehistory> purchasehistories) {
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
        this.purchasehistories = purchasehistories;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "Id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
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
    public Set<Purchasehistory> getPurchasehistories() {
        return this.purchasehistories;
    }

    public void setPurchasehistories(Set<Purchasehistory> purchasehistories) {
        this.purchasehistories = purchasehistories;
    }

}
