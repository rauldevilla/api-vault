package com.techandsolve.apivault.web.filter;

import com.techandsolve.apivault.annotations.CredentialsValidator;
import com.techandsolve.apivault.util.SecurityConfigurationInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class SecurityCookieTokenCredentialsBuilder implements SecurityCredentialsBuilder {

    public static final String SECURITY_CONTEXT_KEY_COOKIE_NAME = "security-cookie-name";

    private static Logger logger = LoggerFactory.getLogger(SecurityCookieTokenCredentialsBuilder.class);

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

    private static Cookie getCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }

        for(Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }

    @Override
    public SecurityContext buildContext(HttpServletRequest request, CredentialsValidator credentialsValidatorAnnotation) {

        if (credentialsValidatorAnnotation.cookieName() == null || credentialsValidatorAnnotation.cookieName().trim().equals("")) {
            logger.warn("CredentialsValidator method has not set a cookieName attribute");
            return null;
        }

        SecurityContext context = new SecurityContext();
        context.setRequest(request);

        Cookie securityCookie = getCookie(request, credentialsValidatorAnnotation.cookieName());
        if (securityCookie == null) {
            logger.warn("Request has not a cookie named " + credentialsValidatorAnnotation.cookieName());
            return null;
        }

        context.put(SECURITY_CONTEXT_KEY_COOKIE_NAME, securityCookie.getValue());

        return context;
    }

}
