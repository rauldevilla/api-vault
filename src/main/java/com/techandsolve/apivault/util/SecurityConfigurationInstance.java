package com.techandsolve.apivault.util;

import com.techandsolve.apivault.exception.ConfigurationException;
import com.techandsolve.apivault.web.filter.Credentials;
import com.techandsolve.apivault.web.filter.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SecurityConfigurationInstance {

    private static Logger logger = LoggerFactory.getLogger(SecurityConfigurationInstance.class);

    private static SecurityConfigurationInstance instance;
    private SecurityConfigurationInstanceValues securityConfigurationInstanceValues;

    private SecurityConfigurationInstance() {
    }

    private synchronized static void loadInstance() throws ConfigurationException {
       instance = new SecurityConfigurationInstance();
       instance.loadConfiguration();
    }

    public static SecurityConfigurationInstance getInstance() throws ConfigurationException {
        if (instance == null) {
            loadInstance();
        }
        return instance;
    }

    private void loadConfiguration() throws ConfigurationException {
        this.securityConfigurationInstanceValues = new SecurityConfigurationInstanceValues();
        this.securityConfigurationInstanceValues.build();
    }

    private boolean executeConfigurationAccessValidationMethod(Method accessValidationMethod, Resource resource) throws IllegalAccessException, InvocationTargetException {
        return (Boolean) this.securityConfigurationInstanceValues.getObjectInspector().invoke(accessValidationMethod, new Object[]{resource});
    }

    private boolean executeConfigurationCredentialsValidationMethod(Method credentiaslValidationMethod, Credentials credentials) throws IllegalAccessException, InvocationTargetException {
        return (Boolean) this.securityConfigurationInstanceValues.getObjectInspector().invoke(credentiaslValidationMethod, new Object[]{credentials});
    }

    public boolean hasAccess(Resource resource) {
        final Method accessValidationMethod = this.securityConfigurationInstanceValues.getAccessValidationMethod();
        final boolean acceptResourcesByDefault = this.securityConfigurationInstanceValues.getAcceptResourcesByDefault();
        if (accessValidationMethod != null) {
            try {
                logger.debug("Validating access to \"" + resource + "\" ...");
                return executeConfigurationAccessValidationMethod(accessValidationMethod, resource);
            } catch (Exception e) {
                logger.error("Error executing Access Validation method " + accessValidationMethod.getName() + " for resource " + resource + ". " +
                             (acceptResourcesByDefault ? "Allowing access" : "Rejecting access") + " by default", e);
                return acceptResourcesByDefault;
            }
        }

        return acceptResourcesByDefault;
    }

    public boolean isAuthenticated(Credentials credentials) {
        final Method credentialsValidationMethod = this.securityConfigurationInstanceValues.getCredentialsValidationMethod();
        final boolean acceptCredentialsByDefault = this.securityConfigurationInstanceValues.getAcceptCredentialsByDefault();
        if (credentialsValidationMethod != null) {
            try {
                logger.debug("Validating credentials \"" + credentials + "\" ...");
                return executeConfigurationCredentialsValidationMethod(credentialsValidationMethod, credentials);
            } catch (Exception e) {
                logger.error("Error executing Credentials Validation method " + credentialsValidationMethod.getName() + " for credentials " + credentials + ". " +
                        (acceptCredentialsByDefault ? "Accepting credentials" : "Rejecting credentials") + " by default", e);
                return acceptCredentialsByDefault;
            }
        }

        return acceptCredentialsByDefault;
    }
}
