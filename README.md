# API-VAULT
**api-vault** is an api for secure web services in a very simple way.

## How to use it?

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
