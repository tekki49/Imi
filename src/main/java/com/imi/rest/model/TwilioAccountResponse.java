package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwilioAccountResponse {
    private String previous_page_uri;

    private String page_size;

    private String next_page_uri;

    private SubAccountDetails[] accounts;

    private String page;

    private String start;

    private String first_page_uri;

    private String uri;

    private String end;

    public String getPrevious_page_uri() {
        return previous_page_uri;
    }

    public void setPrevious_page_uri(String previous_page_uri) {
        this.previous_page_uri = previous_page_uri;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public String getNext_page_uri() {
        return next_page_uri;
    }

    public void setNext_page_uri(String next_page_uri) {
        this.next_page_uri = next_page_uri;
    }

    public SubAccountDetails[] getAccounts() {
        return accounts;
    }

    public void setAccounts(SubAccountDetails[] accounts) {
        this.accounts = accounts;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFirst_page_uri() {
        return first_page_uri;
    }

    public void setFirst_page_uri(String first_page_uri) {
        this.first_page_uri = first_page_uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "ClassPojo [previous_page_uri = " + previous_page_uri
                + ", page_size = " + page_size + ", next_page_uri = "
                + next_page_uri + ", accounts = " + accounts + ", page = "
                + page + ", start = " + start + ", first_page_uri = "
                + first_page_uri + ", uri = " + uri + ", end = " + end + "]";
    }

}
