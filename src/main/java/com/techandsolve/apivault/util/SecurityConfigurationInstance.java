package com.techandsolve.apivault.util;

import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.exception.ConfigurationException;
import com.techandsolve.apivault.web.filter.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    private static Constructor getDefaultConstructor(Class<?> clazz) {
        for (Constructor c : clazz.getDeclaredConstructors()) {
            if (c.getParameterCount() == 0) {
                return c;
            }
        }
        return null;
    }

    private static Object getNewInstanceUsingDefaultConstrutor(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.
                forName(className,
                        true,
                        Thread.currentThread().getContextClassLoader());
        Constructor defaulConstructor = getDefaultConstructor(clazz);

        if (defaulConstructor == null) {
            throw new NoSuchMethodException("Class " + className + " doesn't have a default constructor");
        }

        return defaulConstructor.newInstance(new Object[]{});
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
            this.securityConfigurationObject = getNewInstanceUsingDefaultConstrutor(this.securityConfigurationClass.getName());
            logger.info("Class " + this.securityConfigurationClass.getName() + " has been successfully instantiated");
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ConfigurationException(e);
        }
        this.objectInspector = new ObjectInspector(this.securityConfigurationObject);
    }



    public boolean hasAccess(Resource resource) throws ConfigurationException {
        try {
            Object valueObject = this.objectInspector.getObjectAnnotationAttributeValue(SecurityConfiguration.class, "acceptByDefault");
            return (Boolean) valueObject;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ConfigurationException(e);
        }
    }

}
