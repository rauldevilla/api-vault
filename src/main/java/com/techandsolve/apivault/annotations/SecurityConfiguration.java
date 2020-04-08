package com.techandsolve.apivault.annotations;

import com.techandsolve.apivault.web.filter.Credentials;
import com.techandsolve.apivault.web.filter.BearerTokenCredentials;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SecurityConfiguration {

    boolean acceptResourcesByDefault() default true;
    boolean acceptCredentialsByDefault() default false;
    Class<? extends Credentials> credentialsType() default BearerTokenCredentials.class;


}
