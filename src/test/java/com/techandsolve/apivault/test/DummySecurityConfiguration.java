package com.techandsolve.apivault.test;

import com.techandsolve.apivault.annotations.AccessValidator;
import com.techandsolve.apivault.annotations.CredentialsValidator;
import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.web.filter.*;

@SecurityConfiguration(credentialsBuilders = {SecurityBearerCredentialsBuilder.class})
public class DummySecurityConfiguration {

    @AccessValidator
    public boolean hasAccess(Resource resource, Credentials[] credentials) {
        //HERE, YOUR RESOURCE ACCESS VALIDATION CODE
        //THIS CODE IS JUST AN EXAMPLE
        return resource.getUri().startsWith("/public/");
    }

    @CredentialsValidator
    public boolean isValidCredentials(Credentials credentials) {
        //HERE, YOUR CREDENTIALS VALIDATION CODE
        //THIS CODE IS JUST AN EXAMPLE
        if (credentials instanceof BearerTokenCredentials) {
            return ((BearerTokenCredentials)credentials).getToken().startsWith("VALID-");
        }

        return false;
    }
}