package com.techandsolve.apivault.test;

import com.techandsolve.apivault.web.filter.*;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BearerCredentialsBuilderTest {

    private static Logger logger = LoggerFactory.getLogger(BearerCredentialsBuilderTest.class);

    private static SecurityContext buildBearerCredentialsContext() {
        SecurityContext context = new SecurityContext();
        context.setRequest(new DummyHttpSerlvletRequest());
        return context;
    }

    @Test
    public void testBuildCredentials() {
        SecurityCredentialsBuilder builder = new BearerCredentialsBuilder();
        SecurityContext context = buildBearerCredentialsContext();
        Credentials credentials = builder.build(context);
        logger.debug("Credentials: " + credentials);
        assertThat(credentials).isNotNull();
        assertThat(credentials.toString()).startsWith(BearerCredentialsBuilder.BEARER);
    }

}
