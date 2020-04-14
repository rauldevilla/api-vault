package com.techandsolve.apivault.web.filter;

import com.techandsolve.apivault.annotations.CredentialsValidator;

import javax.servlet.http.HttpServletRequest;

public interface SecurityCredentialsBuilder {

    Credentials build(SecurityContext context);
    SecurityContext buildContext(HttpServletRequest request, CredentialsValidator credentialsValidatorAnnotation);

}
