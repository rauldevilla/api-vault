package com.techandsolve.apivault.web.filter;

public class BearerTokenCredentials implements Credentials {

    public static final String BEARER_TOKEN_LITERAL = "Bearer";

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return BEARER_TOKEN_LITERAL + " " + this.token;
    }
}
