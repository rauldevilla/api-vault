package com.techandsolve.apivault.test;

import com.techandsolve.apivault.annotations.AccessValidator;
import com.techandsolve.apivault.annotations.CredentialsValidator;
import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.web.filter.*;

@SecurityConfiguration(credentialsBuilders = {SecurityBearerCredentialsBuilder.class, SecurityCookieTokenCredentialsBuilder.class})
public class DummySecurityConfiguration {

    @AccessValidator
    public boolean hasAccess(Resource resource, Credentials[] credentials) {
        return resource.getUri().startsWith("/public/");
    }

    @CredentialsValidator
    public boolean isValidCredentials(Credentials credentials) {
        if (credentials instanceof BearerTokenCredentials) {
            return ((BearerTokenCredentials)credentials).getToken().startsWith("VALID-");
        } else if (credentials instanceof CookieTokenCredentials) {
            return ((CookieTokenCredentials)credentials).getToken().startsWith("VALID-");
        }

        return false;
    }
}