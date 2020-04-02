package com.techandsolve.apivault.util;

import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.exception.ConfigurationException;
import com.techandsolve.apivault.web.filter.Resource;

import java.io.IOException;
import java.util.List;

public class SecurityConfigurationInstance {

    private static SecurityConfigurationInstance instance;

    private SecurityConfigurationInstance() {
    }

    private synchronized static void loadInstance() throws ClassNotFoundException, IOException {
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

    private void loadConfiguration() throws ClassNotFoundException, IOException {
        ClassScanner scanner = new ClassScanner();
        List<Class<?>> annotatedClassses = scanner.findClassesWithAnnotation(SecurityConfiguration.class);
    }

    public boolean hasAccess(Resource resource) {
        return true;
    }

}
