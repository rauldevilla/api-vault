package com.techandsolve.apivault.util;

import com.techandsolve.apivault.web.filter.Resource;

public class SecurityConfigurationInstance {

    private static SecurityConfigurationInstance instance;

    private SecurityConfigurationInstance() {
    }

    private synchronized static void loadInstance() {
       instance = new SecurityConfigurationInstance();
       instance.loadConfiguration();
    }

    public static SecurityConfigurationInstance getInstance() {
        if (instance == null) {
            loadInstance();
        }
        return instance;
    }

    private void loadConfiguration() {

    }

    public void hasAccess(Resource resource) {


    }

}
