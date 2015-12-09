package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Recording
{
    private String trim;

    private String mode;

    public String getTrim ()
    {
        return trim;
    }

    public void setTrim (String trim)
    {
        this.trim = trim;
    }

    public String getMode ()
    {
        return mode;
    }

    public void setMode (String mode)
    {
        this.mode = mode;
    }
}