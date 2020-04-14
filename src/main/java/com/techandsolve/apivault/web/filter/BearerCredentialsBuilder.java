package com.techandsolve.apivault.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BearerCredentialsBuilder implements SecurityCredentialsBuilder {

    private static Logger logger = LoggerFactory.getLogger(BearerCredentialsBuilder.class);

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String BEARER = "Bearer ";

    /*
    private static String getTokenFromCookies(SecurityContext context) {
        Cookie[] cookies = context.getRequest().getCookies();
        final String securityCookieName = context.getSecurityCookieName() != null || context.getSecurityCookieName().trim().equals("") ?
                                          context.getSecurityCookieName().trim() :
                                          SecurityContext.SECURITY_COOKIE_NAME;
        if (cookies != null && cookies.length > 0){
            for (Cookie c : cookies) {
                if (securityCookieName.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
    */

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
}
