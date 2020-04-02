package com.techandsolve.apivault.test;

import com.techandsolve.apivault.exception.ConfigurationException;
import com.techandsolve.apivault.web.filter.Resource;
import com.techandsolve.apivault.web.filter.SecurityFilterHelper;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class SecurityFilterHelperTest {

    @Test
    public void testHasAccess() throws ConfigurationException {
        SecurityFilterHelper securityFilterHelper = new SecurityFilterHelper();

        Resource resource = new Resource();
        resource.setUri("/this_is_the_uri");

        boolean hasAccess = securityFilterHelper.hasAccess(resource);

        assertThat(hasAccess).isTrue();
    }

    @Test
    public void testDoesNotHasAccess() throws ConfigurationException {
        SecurityFilterHelper securityFilterHelper = new SecurityFilterHelper();

        Resource resource = new Resource();
        resource.setUri("/this_is_the_uri");

        boolean hasAccess = securityFilterHelper.hasAccess(resource);

        assertThat(hasAccess).isFalse();
    }

}
