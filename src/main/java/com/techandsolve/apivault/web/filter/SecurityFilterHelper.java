package com.techandsolve.apivault.web.filter;

import com.techandsolve.apivault.exception.ConfigurationException;
import com.techandsolve.apivault.util.SecurityConfigurationInstance;

import javax.servlet.http.HttpServletRequest;

public class SecurityFilterHelper {

    public boolean hasAccess(Resource resource) throws ConfigurationException {
        return SecurityConfigurationInstance.getInstance().hasAccess(resource);
    }

    public boolean isAuthenticated(Credentials[] credentials) throws ConfigurationException {
        boolean authenticationStatus;
        for (Credentials c : credentials) {
            authenticationStatus = SecurityConfigurationInstance.getInstance().isAuthenticated(c);
            if (authenticationStatus) {
                return true;
            }
        }
        return false;
    }

    public Credentials[] buildCredentials(HttpServletRequest request) throws ConfigurationException {
        return SecurityConfigurationInstance.getInstance().buildCredentials(request);
    }

}
