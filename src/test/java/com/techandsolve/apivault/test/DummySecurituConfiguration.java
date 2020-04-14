package com.techandsolve.apivault.test;

import com.techandsolve.apivault.annotations.AccessValidator;
import com.techandsolve.apivault.annotations.CredentialsValidator;
import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.web.filter.*;

@SecurityConfiguration(credentialsBuilders = {SecurityBearerCredentialsBuilder.class, SecurityCookieTokenCredentialsBuilder.class})
public class DummySecurituConfiguration {

    @AccessValidator
    public boolean hasAccess(Resource resource) {
        return resource.getUri().startsWith("/public/");
    }

    @CredentialsValidator(cookieName = "dummy-cookie-name")
    public boolean isValidCredentials(BearerTokenCredentials credentials) {
        return credentials.getToken().startsWith("VALID-");
    }

}
