# API-VAULT
**api-vault** is an api for secure web services in a very simple way.

## How to use it?

**api-vault** is based on the concept that you already know how to validate if an user has access to certain resource.  **api-vault** filters the requests to your business api and give you all the information you need to decide whether or not grant the access to the requested resource.  Before that, **api-vault** give you the control to validate if the credentials used to make the invocations are valid.

```mermaid
sequenceDiagram
Requester ->> apivault: execute endpoint
apivault -->> apivault: credentials valid?
Note right of apivault: Excecute method that<br/>your application<br/>offers to<br/>validate credentials.
apivault -->> apivault: Does the credentials have access?
Note right of apivault: Excecute method that<br/>your application<br/>offers to validate<br/>access to resource.
apivault-->>Endpoint: execute