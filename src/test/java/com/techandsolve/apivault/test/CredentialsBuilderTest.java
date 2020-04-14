package com.techandsolve.apivault.test;

import com.techandsolve.apivault.web.filter.*;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;

public class CredentialsBuilderTest {

    private static final String VALID_TOKEN = "VALID-804514e5-b75e-4c08-9463-c2a548e5780a";

    private static Logger logger = LoggerFactory.getLogger(CredentialsBuilderTest.class);

    private static void decorateBearerCredentialsContext(SecurityContext context) {
        DummyHttpSerlvletRequest request = context.getRequest() != null ? (DummyHttpSerlvletRequest) context.getRequest() : new DummyHttpSerlvletRequest();
        request.addHeader(SecurityBearerCredentialsBuilder.AUTHORIZATION_HEADER_NAME, SecurityBearerCredentialsBuilder.BEARER + " " + VALID_TOKEN);
        context.setRequest(request);
    }

    private static void decorateCookiesTokenCredentialsContext(SecurityContext context) {
        final String cookieName = "test-cookie-token";
        DummyHttpSerlvletRequest request = context.getRequest() != null ? (DummyHttpSerlvletRequest) context.getRequest() : new DummyHttpSerlvletRequest();
        request.addCookie(new Cookie(cookieName, VALID_TOKEN));
        context.setRequest(request);
        context.put(SecurityCookieTokenCredentialsBuilder.SECURITY_CONTEXT_KEY_COOKIE_NAME, cookieName);
    }

    @Test
    public void testBuildBearerCredentials() {
        SecurityCredentialsBuilder builder = new SecurityBearerCredentialsBuilder();
        SecurityContext context = new SecurityContext();
        decorateBearerCredentialsContext(context);
        Credentials credentials = builder.build(context);
        logger.debug("Credentials: " + credentials);
        assertThat(credentials).isNotNull();
        assertThat(credentials.toString()).startsWith(SecurityBearerCredentialsBuilder.BEARER);
    }

    @Test
    public void testBuildCookiesTokenCredentials() {
        SecurityCredentialsBuilder builder = new SecurityCookieTokenCredentialsBuilder();
        SecurityContext context = new SecurityContext();
        decorateCookiesTokenCredentialsContext(context);
        Credentials credentials = builder.build(context);
        logger.debug("Credentials: " + credentials);
        assertThat(credentials).isNotNull();
    }
}
