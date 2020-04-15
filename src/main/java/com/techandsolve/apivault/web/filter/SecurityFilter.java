package com.techandsolve.apivault.web.filter;

import com.techandsolve.apivault.exception.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    private SecurityFilterHelper helper = new SecurityFilterHelper();

    private FilterConfig config;

    protected FilterConfig getFilterConfig() {
        return this.config;
    }

    @Override
    public final void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

    private boolean hasAccess(String uri) {
        Resource resource = new Resource();
        resource.setUri(uri);
        try {
            return this.helper.hasAccess(resource);
        } catch (ConfigurationException e) {
            logger.error("Exception in hasAccess", e);
            return false;
        }
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        Credentials[] credentials;

        try {
            credentials = this.helper.buildCredentials(request);
        } catch (ConfigurationException e) {
            logger.error("Error creating credentials", e);
            return false;
        }

        try {
            return this.helper.isAuthenticated(credentials);
        } catch (ConfigurationException e) {
            logger.error("Error validating credentials " + credentials, e);
            return false;
        }
    }

    private static String getURI(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI();
    }

    protected static String getRemoteAddress(HttpServletRequest request) {
        String remoteAddress = request.getHeader("X-FORWARDED-FOR");
        if (remoteAddress == null || "".equals(remoteAddress.trim())) {
            remoteAddress = request.getRemoteAddr();
        }
        return remoteAddress;
    }

    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!isAuthenticated(httpRequest)) {
            logger.warn("Invalid authentication from " + getRemoteAddress(httpRequest));
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String uri = getURI(httpRequest);
        if (!hasAccess(uri)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }

}
