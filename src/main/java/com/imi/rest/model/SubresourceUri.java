package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SubresourceUri
{
    private String recordings;

    private String sms_messages;

    private String conferences;

    private String calls;

    private String outgoing_caller_ids;

    private String messages;

    private String authorized_connect_apps;

    private String sip;

    private String connect_apps;

    private String notifications;

    private String sandbox;

    private String usage;

    private String available_phone_numbers;

    private String transcriptions;

    private String applications;

    private String queues;

    private String incoming_phone_numbers;

    private String media;

    public String getRecordings ()
    {
        return recordings;
    }

    public void setRecordings (String recordings)
    {
        this.recordings = recordings;
    }

    public String getSms_messages ()
    {
        return sms_messages;
    }

    public void setSms_messages (String sms_messages)
    {
        this.sms_messages = sms_messages;
    }

    public String getConferences ()
    {
        return conferences;
    }

    public void setConferences (String conferences)
    {
        this.conferences = conferences;
    }

    public String getCalls ()
    {
        return calls;
    }

    public void setCalls (String calls)
    {
        this.calls = calls;
    }

    public String getOutgoing_caller_ids ()
    {
        return outgoing_caller_ids;
    }

    public void setOutgoing_caller_ids (String outgoing_caller_ids)
    {
        this.outgoing_caller_ids = outgoing_caller_ids;
    }

    public String getMessages ()
    {
        return messages;
    }

    public void setMessages (String messages)
    {
        this.messages = messages;
    }

    public String getAuthorized_connect_apps ()
    {
        return authorized_connect_apps;
    }

    public void setAuthorized_connect_apps (String authorized_connect_apps)
    {
        this.authorized_connect_apps = authorized_connect_apps;
    }

    public String getSip ()
    {
        return sip;
    }

    public void setSip (String sip)
    {
        this.sip = sip;
    }

    public String getConnect_apps ()
    {
        return connect_apps;
    }

    public void setConnect_apps (String connect_apps)
    {
        this.connect_apps = connect_apps;
    }

    public String getNotifications ()
    {
        return notifications;
    }

    public void setNotifications (String notifications)
    {
        this.notifications = notifications;
    }

    public String getSandbox ()
    {
        return sandbox;
    }

    public void setSandbox (String sandbox)
    {
        this.sandbox = sandbox;
    }

    public String getUsage ()
    {
        return usage;
    }

    public void setUsage (String usage)
    {
        this.usage = usage;
    }

    public String getAvailable_phone_numbers ()
    {
        return available_phone_numbers;
    }

    public void setAvailable_phone_numbers (String available_phone_numbers)
    {
        this.available_phone_numbers = available_phone_numbers;
    }

    public String getTranscriptions ()
    {
        return transcriptions;
    }

    public void setTranscriptions (String transcriptions)
    {
        this.transcriptions = transcriptions;
    }

    public String getApplications ()
    {
        return applications;
    }

    public void setApplications (String applications)
    {
        this.applications = applications;
    }

    public String getQueues ()
    {
        return queues;
    }

    public void setQueues (String queues)
    {
        this.queues = queues;
    }

    public String getIncoming_phone_numbers ()
    {
        return incoming_phone_numbers;
    }

    public void setIncoming_phone_numbers (String incoming_phone_numbers)
    {
        this.incoming_phone_numbers = incoming_phone_numbers;
    }

    public String getMedia ()
    {
        return media;
    }

    public void setMedia (String media)
    {
        this.media = media;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [recordings = "+recordings+", sms_messages = "+sms_messages+", conferences = "+conferences+", calls = "+calls+", outgoing_caller_ids = "+outgoing_caller_ids+", messages = "+messages+", authorized_connect_apps = "+authorized_connect_apps+", sip = "+sip+", connect_apps = "+connect_apps+", notifications = "+notifications+", sandbox = "+sandbox+", usage = "+usage+", available_phone_numbers = "+available_phone_numbers+", transcriptions = "+transcriptions+", applications = "+applications+", queues = "+queues+", incoming_phone_numbers = "+incoming_phone_numbers+", media = "+media+"]";
    }
}