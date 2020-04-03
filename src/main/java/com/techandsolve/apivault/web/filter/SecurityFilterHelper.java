package com.techandsolve.apivault.web.filter;

import com.techandsolve.apivault.exception.ConfigurationException;
import com.techandsolve.apivault.util.SecurityConfigurationInstance;

public class SecurityFilterHelper {

    public boolean hasAccess(Resource resource) throws ConfigurationException {
        return SecurityConfigurationInstance.getInstance().hasAccess(resource);
    }

    public boolean validaCredentials(Credentials credentials) {
        return true;
    }

}
