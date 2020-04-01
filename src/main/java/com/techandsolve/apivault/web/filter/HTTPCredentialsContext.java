package com.techandsolve.apivault.web.filter;

import javax.servlet.http.HttpServletRequest;

public class HTTPCredentialsContext {

    private HttpServletRequest request;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
