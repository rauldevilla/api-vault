package com.techandsolve.apivault.annotations;

import com.techandsolve.apivault.web.filter.Credentials;
import com.techandsolve.apivault.web.filter.TokenCredentials;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SecurityConfiguration {

    boolean acceptByDefault() default true;
    Class<? extends Credentials> credentialsType() default TokenCredentials.class;


}
