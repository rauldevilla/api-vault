package com.techandsolve.apivault.test;

import com.techandsolve.apivault.annotations.AccessValidator;
import com.techandsolve.apivault.annotations.CredentialsValidator;
import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.web.filter.Resource;
import com.techandsolve.apivault.web.filter.BearerTokenCredentials;

@SecurityConfiguration
public class DummySecurituConfiguration {

    @AccessValidator
    public boolean hasAccess(Resource resource) {
        return resource.getUri().startsWith("/public/");
    }

    @CredentialsValidator
    public boolean isValidCredentials(BearerTokenCredentials credentials) {
        return credentials.getBearerToken().startsWith("VALID-");
    }

}
