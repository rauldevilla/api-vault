
# API-VAULT
**api-vault** is an api for secure web services in a very simple way.

## How does it work?

**api-vault** is based on the concept that you already know how to validate if an user has access to certain resource.  **api-vault** filters the requests to your business api and give you all the information you need to decide whether or not grant the access to the requested resource.  Before that, **api-vault** give you the control to validate if the credentials used to make the invocations are valid.

```
Requester                            api-vault                                      end point
---------                            ---------                                      ---------
    |       1. access to resource        |                                               |
    | ---------------------------------->|                                               |
    |                                    |                                               |
    |                                    |----                                           |
    |                                    |    | 2. validate credentials                  |
    |                                    |    |                                          |
    |                                    |<---                                           |
    |                                    |                                               |
    |                                    |                                               |
    |                                    |----                                           |
    |                                    |    | 3. validate access to resource           |
    |                                    |    |                                          |
    |                                    |<---                                           |
    |                                    |                                               |
    |                                    |          4. complete the chain                |
    |                                    |---------------------------------------------->|
    |                                    |                                               |
    |                                    |                                               |

```

In the previous diagram:

#### 1. access to resource
The Requester (e.i an api client) makes an http invocation.

#### 2. validate credentials
**api-vault** intercepts the request (using a [java servlet filter](https://docs.oracle.com/javaee/6/api/javax/servlet/Filter.html)), create the associated security credentials and invokes your custom method to validate those credentials.  If this invocation fails, then the invoker receives an 403 http error.

#### 3. validate access to resource
Once **api-vault** knows that the credentials are valid, it invokes your custom method to validate the access to the requested resource.  If this validations fails, then the invoker receives an 403 http error.

#### 4. complete the chain
The request is delegated to the requested endpoint and it will do its job.

## How do I use it in an Spring boot application?

### 1. Configuration
First, you have to implements the configuration class.  This class is very important, because it implements the methods **api-vault** invokes to validate the credentials and the access to any resource you want to secure in your application.

```java
import com.techandsolve.apivault.annotations.AccessValidator;
import com.techandsolve.apivault.annotations.CredentialsValidator;
import com.techandsolve.apivault.annotations.SecurityConfiguration;
import com.techandsolve.apivault.web.filter.*;

@SecurityConfiguration(credentialsBuilders = {SecurityBearerCredentialsBuilder.class})
public class MyApiVaultConfigurationClass {

    @AccessValidator
    public boolean hasAccess(Resource resource, Credentials[] credentials) {
        //HERE, YOUR RESOURCE ACCESS VALIDATION CODE
        //THIS CODE IS JUST AN EXAMPLE
        return resource.getUri().startsWith("/public/");
    }

    @CredentialsValidator
    public boolean isValidCredentials(Credentials credentials) {
        //HERE, YOUR CREDENTIALS VALIDATION CODE
        //THIS CODE IS JUST AN EXAMPLE
        if (credentials instanceof BearerTokenCredentials) {
            return ((BearerTokenCredentials)credentials).getToken().startsWith("VALID-");
        }

        return false;
    }
}
```

#### `@SecurityConfiguration` annotation
This annotation indicates that the annotated class will perform as **api-vault** framework configuration class.  The following table contains the list of attributes for `@SecurityConfiguration`


| **Attribute** | **Description** |  **Allowed values** |
|--|--|--|
| `acceptResourcesByDefault` | Indicates whether or not allow access, in case the `@AccessValidator` fails, or it was not implemented. | `true` / `false`.  Default value `true` |
| `acceptCredentialsByDefault` | Indicates whether or not qualify the received credentials as valid, in case the `@CredentialsValidator` fails, or it was not implemented. | `true` / `false`.  Default value `false` |
| `credentialsBuilders` | Specifies which classes will be used to build the credentials contained in the [HttpServletRequest](https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html) sent from the client of your api. The build order used is the same order as you specify the values of this property | There are two Out-of-the-box Credentials Builder Implementations: `com.techandsolve.apivault.web.filter.SecurityBearerCredentialsBuilder` and `com.techandsolve.apivault.web.filter.SecurityCookieTokenCredentialsBuilder`.  This property is required and it has not default value |


#### Credentials Validation Method
The Credentials Validation Method  is the one used to verify whether or not the credentials sent by the invoker in the [HttpServletRequest](https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html) are valid.  The following table details the required signature to the method:

| **Method Element** | **Description** |
|--|--|
| Annotations | It must to be annotated with `com.techandsolve.apivault.annotations.CredentialsValidator` |
| Name | As you wish. I suggest to use clean code naming rules |
| Access modifier | It must to be `public` |
| Return type | It must to be `boolean` |
| Parameters | This method only receives one parameter of type `com.techandsolve.apivault.web.filter.Credentials` |

As you can see, the method must to be annotated with `@CredentialsValidator`.  This annotation only have one optional attribute, `cookieName`.  This is used when you have one `credentialsBuilder`of the type `com.techandsolve.apivault.web.filter.SecurityCookieTokenCredentialsBuilder` to specify the name of the cookie in the [HttpServletRequest](https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html) which contains the value of the authentication token that is going to be validated.

#### Access Validation Method
 This is the method who give you the control to grant or reject the access to every specific URL resource in your api.  The Access Validation Method has the following structure:

| **Method Element** | **Description** |
|--|--|
| Annotations | It must to be annotated with `com.techandsolve.apivault.annotations.AccessValidator` |
| Name | As you wish. I suggest to use clean code naming rules |
| Access modifier | It must to be `public` |
| Return type | It must to be `boolean` |
| Parameters | This method receives two parameters: The first one has ``com.techandsolve.apivault.web.filter.Resource`` type. It indicates the resource who is been requested, i.e: ``/client/12345``.  Second, is an array of ``com.techandsolve.apivault.web.filter.Credential``.  It indicates all the valid credentials that the requester send in the [HttpServletRequest](https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html).  The concrete object type in this array depends of the configuration of the parameter ``credentialsBuilders`` of the annotation ``com.techandsolve.apivault.annotations.SecurityConfiguration``  |

### 2. Filter

Now it's time to use the framework.  This is the most simple part.  Once you implemented the Configuration class, it's time to configure a servlet filter in your application.  This servlet filter must extends the class ``com.techandsolve.apivault.web.filter.SecurityFilter``.  The following example, is a real world example, and yes, is that simple:

````java
package com.apivault.example;

import com.techandsolve.apivault.web.filter.SecurityFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class MainSecurityFilter extends SecurityFilter {
}
````

This is a filter in an [Spring Boot](https://spring.io/projects/spring-boot) microservice.  That's why you don't see the [@WebFilter](https://docs.oracle.com/javaee/7/api/javax/servlet/annotation/WebFilter.html) annotation.
