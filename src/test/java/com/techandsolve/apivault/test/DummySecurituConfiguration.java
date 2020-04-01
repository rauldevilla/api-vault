package com.techandsolve.apivault.test;

import com.techandsolve.apivault.annotations.AccessValidator;
import com.techandsolve.apivault.annotations.CredentialsValidator;
import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.web.filter.Credentials;
import com.techandsolve.apivault.web.filter.Resource;
import com.techandsolve.apivault.web.filter.TokenCredentials;

@SecurityConfiguration
public class DummySecurituConfiguration {

    @AccessValidator
    public boolean hasAccess(Resource resource) {
        return true;
    }

    @CredentialsValidator
    public boolean isValidCredentials(TokenCredentials credentials) {
        return true;
    }

}
