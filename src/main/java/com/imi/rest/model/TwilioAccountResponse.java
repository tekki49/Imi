package com.imi.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwilioAccountResponse {

    private String first_page_uri;
    private int end;
    private String previous_page_uri;
    private String uri;
    private int page_size;
    private int start;
    private List<TwilioAccount> accounts;
    private String next_page_ur;
    private int page;

    public String getFirst_page_uri() {
        return first_page_uri;
    }

    public void setFirst_page_uri(String first_page_uri) {
        this.first_page_uri = first_page_uri;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getPrevious_page_uri() {
        return previous_page_uri;
    }

    public void setPrevious_page_uri(String previous_page_uri) {
        this.previous_page_uri = previous_page_uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<TwilioAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<TwilioAccount> accounts) {
        this.accounts = accounts;
    }

    public String getNext_page_ur() {
        return next_page_ur;
    }

    public void setNext_page_ur(String next_page_ur) {
        this.next_page_ur = next_page_ur;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
