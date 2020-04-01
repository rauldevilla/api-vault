package com.techandsolve.apivault.web.filter;

public class TokenCredentialsProcessor implements CredentialsProcessor<TokenCredentials> {

    @Override
    public boolean isValidCredentials(TokenCredentials credentials) {
        return false;
    }
}
