package com.techandsolve.apivault.web.filter;

import com.techandsolve.apivault.annotations.CredentialsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class SecurityBearerCredentialsBuilder implements SecurityCredentialsBuilder {

    private static Logger logger = LoggerFactory.getLogger(SecurityBearerCredentialsBuilder.class);

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String BEARER = "Bearer ";

    private static String getTokenFromHeaders(SecurityContext context) {
        String header = context.getRequest().getHeader(AUTHORIZATION_HEADER_NAME);

        if (header == null || header.trim().equals("")) {
            return null;
        }
        header = header.trim();
        if (!header.startsWith(BEARER)) {
            return null;
        }

        return header.substring(BEARER.length()).trim();
    }

    private static String getToken(SecurityContext context) {
        String token = getTokenFromHeaders(context);
        logger.debug("Token: " + token);
        return token;
    }

    @Override
    public Credentials build(SecurityContext context) {
        BearerTokenCredentials credentials = new BearerTokenCredentials();
        credentials.setToken(getToken(context));
        return credentials;
    }

    @Override
    public SecurityContext buildContext(HttpServletRequest request, CredentialsValidator credentialsValidatorAnnotation) {
        SecurityContext context = new SecurityContext();
        context.setRequest(request);
        return context;
    }
}
