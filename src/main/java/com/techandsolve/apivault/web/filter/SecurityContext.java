package com.techandsolve.apivault.web.filter;

import javax.servlet.http.HttpServletRequest;

public class SecurityContext {
    public static final String SECURITY_COOKIE_NAME = "api-vault-stc";

    private HttpServletRequest request;
    private boolean tokenInCookies = false;
    private String securityCookieName = SECURITY_COOKIE_NAME;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public boolean isTokenInCookies() {
        return tokenInCookies;
    }

    public void setTokenInCookies(boolean tokenInCookies) {
        this.tokenInCookies = tokenInCookies;
    }

    public String getSecurityCookieName() {
        return securityCookieName;
    }

    public void setSecurityCookieName(String securityCookieName) {
        this.securityCookieName = securityCookieName;
    }
}
