package com.techandsolve.apivault.web.filter;

public interface CredentialsProcessor<T extends Credentials> {

    boolean validCredentials(T credentials);

}
