package com.techandsolve.apivault.util;

import com.techandsolve.apivault.annotations.AccessValidator;
import com.techandsolve.apivault.annotations.CredentialsValidator;
import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.exception.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

class SecurityConfigurationInstanceValues {

    private static Logger logger = LoggerFactory.getLogger(SecurityConfigurationInstanceValues.class);

    private boolean acceptResourcesByDefault = true;
    private boolean acceptCredentialsByDefault = false;
    private Class<?> securityConfigurationClass;
    private Object securityConfigurationObject;
    private Method accessValidationMethod;
    private Method credentialsValidationMethod;
    private ObjectInspector objectInspector;

    boolean getAcceptResourcesByDefault() {
        return this.acceptResourcesByDefault;
    }

    boolean getAcceptCredentialsByDefault() {
        return this.acceptCredentialsByDefault;
    }

    Method getAccessValidationMethod() {
        return accessValidationMethod;
    }

    Method getCredentialsValidationMethod() {
        return credentialsValidationMethod;
    }

    ObjectInspector getObjectInspector() {
        return this.objectInspector;
    }

    void loadSecurityConfigurationClass() throws ConfigurationException {
        ClassScanner scanner = new ClassScanner();
        List<Class<?>> annotatedClassses;

        try {
            annotatedClassses = scanner.findClassesWithAnnotation(SecurityConfiguration.class);
        } catch (ClassNotFoundException | IOException e) {
            throw new ConfigurationException(e);
        }

        if (annotatedClassses == null || annotatedClassses.size() == 0) {
            throw new ConfigurationException("SecurityConfiguration not found");
        }

        this.securityConfigurationClass = annotatedClassses.get(0);
        if (annotatedClassses.size() > 1) {
            for (Class<?> c : annotatedClassses) {
                logger.warn("SecurityConfiguration found in class " + c.getName());
            }
        }
        logger.info("Working with SecurityConfiguration: " + this.securityConfigurationClass.getName());
    }

    void loadSecurityConfigurationObject() throws ConfigurationException {
        try {
            this.securityConfigurationObject = ObjectInspector.getNewInstanceUsingDefaultConstrutor(this.securityConfigurationClass.getName());
            logger.info("Class " + this.securityConfigurationClass.getName() + " has been successfully instantiated");
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ConfigurationException(e);
        }
        this.objectInspector = new ObjectInspector(this.securityConfigurationObject);
    }

    void loadAccessValidationMethod() throws ConfigurationException {
        List<Method> methods;

        try {
            methods = this.objectInspector.getAnnotatedMethods(AccessValidator.class);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException(e);
        }
        this.accessValidationMethod = null;

        if (methods == null || methods.size() == 0) {
            logger.warn("Access validation method not found");
            return;
        }

        this.accessValidationMethod = methods.get(0);
        if (methods.size() > 1) {
            logger.warn("More than ONE Access Validation method in class " + this.securityConfigurationClass.getName());
            for (Method m : methods) {
                logger.warn("Annotated method found: " + m.getName());
            }
        }

        logger.info("Using Access Validation method: " + accessValidationMethod.getName());
    }

    void loadCredentialsValidationMethod() throws ConfigurationException {
        List<Method> methods;

        try {
            methods = this.objectInspector.getAnnotatedMethods(CredentialsValidator.class);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException(e);
        }

        this.credentialsValidationMethod = null;

        if (methods == null || methods.size() == 0) {
            logger.warn("Credentials validation method not found");
            return;
        }

        this.credentialsValidationMethod = methods.get(0);
        if (methods.size() > 1) {
            logger.warn("More than ONE Credentials Validation method in class " + this.securityConfigurationClass.getName());
            for (Method m : methods) {
                logger.warn("Annotated method found: " + m.getName());
            }
        }

        logger.info("Using Credentials Validation method: " + credentialsValidationMethod.getName());
    }

    void loadAcceptResourcesByDefault() throws ConfigurationException {
        try {
            this.acceptResourcesByDefault = (Boolean) this.objectInspector.getObjectAnnotationAttributeValue(SecurityConfiguration.class, "acceptResourcesByDefault");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ConfigurationException(e);
        }
    }

    void loadAcceptCredentialsByDefault() throws ConfigurationException {
        try {
            this.acceptCredentialsByDefault = (Boolean) this.objectInspector.getObjectAnnotationAttributeValue(SecurityConfiguration.class, "acceptCredentialsByDefault");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ConfigurationException(e);
        }
    }

    void build() throws ConfigurationException {
        loadSecurityConfigurationClass();
        loadSecurityConfigurationObject();
        loadAcceptResourcesByDefault();
        loadAcceptCredentialsByDefault();
        loadAccessValidationMethod();
        loadCredentialsValidationMethod();
    }

}
