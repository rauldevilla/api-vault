package com.techandsolve.apivault.test;

import com.techandsolve.apivault.exception.ConfigurationException;
import com.techandsolve.apivault.web.filter.BearerTokenCredentials;
import com.techandsolve.apivault.web.filter.Credentials;
import com.techandsolve.apivault.web.filter.Resource;
import com.techandsolve.apivault.web.filter.SecurityFilterHelper;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class SecurityFilterHelperTest {

    @Test
    public void testHasAccess() throws ConfigurationException {
        SecurityFilterHelper securityFilterHelper = new SecurityFilterHelper();

        Resource resource = new Resource();
        resource.setUri("/public/this_is_the_uri");

        boolean hasAccess = securityFilterHelper.hasAccess(resource);

        assertThat(hasAccess).isTrue();
    }

    @Test
    public void testDoesNotHasAccess() throws ConfigurationException {
        SecurityFilterHelper securityFilterHelper = new SecurityFilterHelper();

        Resource resource = new Resource();
        resource.setUri("/private/this_is_the_uri_2");

        boolean hasAccess = securityFilterHelper.hasAccess(resource);

        assertThat(hasAccess).isFalse();
    }

    private static Credentials buildBearerTokenCredentials(String bearerToken) {
        BearerTokenCredentials credentials = new BearerTokenCredentials();
        credentials.setToken(bearerToken);
        return credentials;
    }

    private static Credentials buildValidBearerTokenCredentials() {
        return buildBearerTokenCredentials("VALID-804514e5-b75e-4c08-9463-c2a548e5780a");
    }

    private static Credentials buildInvalidBearerTokenCredentials() {
        return buildBearerTokenCredentials("INVALID-804514e5-b75e-4c08-9463-c2a548e5780a");
    }

    @Test
    public void testValidBearerTokenCredentials() throws ConfigurationException {
        SecurityFilterHelper securityFilterHelper = new SecurityFilterHelper();
        Credentials[] credentials = new Credentials[]{buildValidBearerTokenCredentials()};
        boolean isValid = securityFilterHelper.isAuthenticated(credentials);
        assertThat(isValid).isTrue();
    }

    @Test
    public void testInvalidBearerTokenCredentials() throws ConfigurationException {
        SecurityFilterHelper securityFilterHelper = new SecurityFilterHelper();
        Credentials[] credentials = new Credentials[]{buildInvalidBearerTokenCredentials()};
        boolean isValid = securityFilterHelper.isAuthenticated(credentials);
        assertThat(isValid).isFalse();
    }
}
