package com.techandsolve.apivault.web.filter;

public class Resource {

    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return this.uri;
    }
}
