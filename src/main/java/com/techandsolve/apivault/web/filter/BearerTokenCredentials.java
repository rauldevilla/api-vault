package com.techandsolve.apivault.web.filter;

public class BearerTokenCredentials implements Credentials {

    public static final String BEARER_TOKEN_LITERAL = "Bearer";

    private String bearerToken;

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    @Override
    public String toString() {
        return BEARER_TOKEN_LITERAL + " " + this.bearerToken;
    }
}
