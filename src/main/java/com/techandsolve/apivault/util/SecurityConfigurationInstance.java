package com.techandsolve.apivault.util;

import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.exception.ConfigurationException;
import com.techandsolve.apivault.web.filter.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class SecurityConfigurationInstance {

    private static Logger logger = LoggerFactory.getLogger(SecurityConfigurationInstance.class);

    private static SecurityConfigurationInstance instance;
    private Class<?> securityConfigurationClass;
    private Object securityConfigurationObject;

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

    private void loadConfiguration() throws ClassNotFoundException, IOException, ConfigurationException {
        ClassScanner scanner = new ClassScanner();
        List<Class<?>> annotatedClassses = scanner.findClassesWithAnnotation(SecurityConfiguration.class);

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
        this.securityConfigurationObject = Class.forName(this.securityConfigurationClass.getName(), true, Thread.currentThread().getContextClassLoader());
    }

    public boolean hasAccess(Resource resource) {
        return true;
    }

}
