package com.techandsolve.apivault.util;

import com.techandsolve.apivault.annotations.AccessValidator;
import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.exception.ConfigurationException;
import com.techandsolve.apivault.web.filter.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SecurityConfigurationInstance {

    private static Logger logger = LoggerFactory.getLogger(SecurityConfigurationInstance.class);

    private static SecurityConfigurationInstance instance;
    private Class<?> securityConfigurationClass;
    private Object securityConfigurationObject;
    private ObjectInspector objectInspector;

    private SecurityConfigurationInstance() {
    }

    private synchronized static void loadInstance() throws ClassNotFoundException, IOException, ConfigurationException {
       instance = new SecurityConfigurationInstance();
       instance.loadConfiguration();
    }

    public static SecurityConfigurationInstance getInstance() throws ConfigurationException {
        if (instance == null) {
            try {
                loadInstance();
            } catch (ClassNotFoundException | IOException e) {
                throw new ConfigurationException(e);
            }
        }
        return instance;
    }

    private void loadConfiguration() throws ConfigurationException {
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

        try {
            this.securityConfigurationObject = ObjectInspector.getNewInstanceUsingDefaultConstrutor(this.securityConfigurationClass.getName());
            logger.info("Class " + this.securityConfigurationClass.getName() + " has been successfully instantiated");
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ConfigurationException(e);
        }
        this.objectInspector = new ObjectInspector(this.securityConfigurationObject);
    }


    private Method getAccessValidationMethod() {
        List<Method> methods = this.objectInspector.getAnnotatedMethods(AccessValidator.class);

        if (methods == null || methods.size() == 0) {
            return null;
        }

        Method accessValidationMethod = methods.get(0);
        if (methods.size() > 0) {
            logger.warn("More than ONE Access Validation method in class " + this.securityConfigurationClass.getName());
            for (Method m : methods) {
                logger.warn("Annotated method found: " + m.getName());
            }
        }

        logger.info("Using Access Validation method: " + accessValidationMethod.getName());

        return accessValidationMethod;
    }

    public boolean executeConfigurationAccessValidationMethod(Method accessValidationMethod, Resource resource) throws IllegalAccessException, InvocationTargetException {
        return (Boolean) this.objectInspector.invoke(accessValidationMethod, new Object[]{resource});
    }

    public boolean hasAccess(Resource resource) throws ConfigurationException {

        Object valueObject;
        boolean acceptByDefault;

        try {
            valueObject = this.objectInspector.getObjectAnnotationAttributeValue(SecurityConfiguration.class, "acceptByDefault");
            acceptByDefault = (Boolean) valueObject;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ConfigurationException(e);
        }

        Method accessValidationMethod = getAccessValidationMethod();
        if (accessValidationMethod != null) {
            try {
                return executeConfigurationAccessValidationMethod(accessValidationMethod, resource);
            } catch (Exception e) {
                logger.error("Error executing Access Validation method " + accessValidationMethod.getName() + " for resource " + resource + ". " +
                             (acceptByDefault ? "Allowing access" : "Rejecting access") + " by default", e);
                return acceptByDefault;
            }
        }

        return acceptByDefault;
    }

}
