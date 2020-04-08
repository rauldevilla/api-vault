package com.techandsolve.apivault.web.filter;

public class BearerTokenCredentialsProcessor implements CredentialsProcessor<BearerTokenCredentials> {

    @Override
    public boolean validCredentials(BearerTokenCredentials credentials) {
        return false;
    }
}
