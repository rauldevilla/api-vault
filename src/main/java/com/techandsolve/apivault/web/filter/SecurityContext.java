package com.techandsolve.apivault.web.filter;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SecurityContext {

    private HttpServletRequest request;
    private Map<String, Object> bundle = new HashMap<>();

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void put(String key, Object value) {
        this.bundle.put(key, value);
    }

    public Object get(String key) {
        return this.bundle.get(key);
    }

    public String getAsString(String key) {
        return (String) this.get(key);
    }

    public Collection<String> getKeys() {
        return this.bundle.keySet();
    }
}
