package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {
    private int limit;
    private int offset;
    private String previous;
    private String next;
    private int total;
    private String previousPlivoIndex;
    private String previousNexmoIndex;
    private String nextPlivoIndex;
    private String nextNexmoIndex;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @JsonIgnore
    public int getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(int total) {
        this.total = total;
    }

    @JsonProperty("total_count")
    public void setTotalCount(int total) {
        this.total = total;
    }

    public String getPreviousPlivoIndex() {
        return previousPlivoIndex;
    }

    public void setPreviousPlivoIndex(String previousPlivoIndex) {
        this.previousPlivoIndex = previousPlivoIndex;
    }

    public String getPreviousNexmoIndex() {
        return previousNexmoIndex;
    }

    public void setPreviousNexmoIndex(String previousNexmoIndex) {
        this.previousNexmoIndex = previousNexmoIndex;
    }

    public String getNextPlivoIndex() {
        return nextPlivoIndex;
    }

    public void setNextPlivoIndex(String nextPlivoIndex) {
        this.nextPlivoIndex = nextPlivoIndex;
    }

    public String getNextNexmoIndex() {
        return nextNexmoIndex;
    }

    public void setNextNexmoIndex(String nextNexmoIndex) {
        this.nextNexmoIndex = nextNexmoIndex;
    }

}
