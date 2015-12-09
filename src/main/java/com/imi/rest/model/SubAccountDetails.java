package com.imi.rest.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubAccountDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String sid;

    private String status;

    private String date_updated;

    private String auth_token;

    private String date_created;

    private String friendly_name;

    private String owner_account_sid;

    private String type;

    private SubresourceUri subresource_uris;

    private String uri;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getFriendly_name() {
        return friendly_name;
    }

    public void setFriendly_name(String friendly_name) {
        this.friendly_name = friendly_name;
    }

    public String getOwner_account_sid() {
        return owner_account_sid;
    }

    public void setOwner_account_sid(String owner_account_sid) {
        this.owner_account_sid = owner_account_sid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SubresourceUri getSubresource_uris() {
        return subresource_uris;
    }

    public void setSubresource_uris(SubresourceUri subresource_uris) {
        this.subresource_uris = subresource_uris;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
