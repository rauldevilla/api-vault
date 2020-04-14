package com.techandsolve.apivault.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;

public class CookieTokenCredentialsBuilder implements SecurityCredentialsBuilder {

    public static final String DEFAULT_COOKIE_NAME = "api-vault-stc";
    public static final String SECURITY_CONTEXT_KEY_COOKIE_NAME = "security-cookie-name";

    private static Logger logger = LoggerFactory.getLogger(CookieTokenCredentialsBuilder.class);

    private static String getSecurityCookieName(SecurityContext context) {
        final String cookieName = context.getAsString(SECURITY_CONTEXT_KEY_COOKIE_NAME);
        return cookieName != null ? cookieName.trim() : null;
    }

    private static String getTokenFromCookies(SecurityContext context) {
        Cookie[] cookies = context.getRequest().getCookies();
        final String securityCookieName = getSecurityCookieName(context);
        if (cookies != null && cookies.length > 0){
            for (Cookie c : cookies) {
                if (securityCookieName.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    private static String getToken(SecurityContext context) {
        String token = getTokenFromCookies(context);
        logger.debug("Token: " + token);
        return token;
    }

    @Override
    public Credentials build(SecurityContext context) {
        CookieTokenCredentials credentials = new CookieTokenCredentials();
        credentials.setToken(getToken(context));
        credentials.setCookieName(getSecurityCookieName(context));
        return credentials;
    }
}
