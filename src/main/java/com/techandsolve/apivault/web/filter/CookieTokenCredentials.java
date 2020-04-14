package com.techandsolve.apivault.web.filter;

public class CookieTokenCredentials implements Credentials {

    private String cookieName;
    private String token;

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return this.cookieName + "=" + this.token;
    }
}
