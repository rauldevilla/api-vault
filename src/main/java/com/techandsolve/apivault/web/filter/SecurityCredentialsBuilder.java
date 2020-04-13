package com.techandsolve.apivault.web.filter;

public interface SecurityCredentialsBuilder {

    Credentials build(SecurityContext context);

}
