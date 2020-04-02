package com.techandsolve.apivault.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityFilter implements Filter {

    private SecurityFilterHelper helper;

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
        return this.helper.hasAccess(resource);
    }

    private static String getURI(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI();
    }

    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = getURI(httpRequest);
        if (!hasAccess(uri)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }

}
