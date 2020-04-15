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
        return resource.getUri().startsWith("/public/");
    }

    @CredentialsValidator
    public boolean isValidCredentials(Credentials credentials) {

        if (credentials instanceof BearerTokenCredentials) {
            return ((BearerTokenCredentials)credentials).getToken().startsWith("VALID-");
        }

        return false;
    }
}
```


### 2. Filter


