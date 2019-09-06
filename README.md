[![Build Status][travis-badge]][travis-badge-url]
[![Quality Gate][sonarqube-badge]][sonarqube-badge-url] 
[![Technical debt ratio][technical-debt-ratio-badge]][technical-debt-ratio-badge-url] 
[![Coverage][coverage-badge]][coverage-badge-url]

![](./img/spring-cloud-gateway.svg)
 
Spring Cloud Gateway Example 
==============================
This project is example of using [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway) as an edge 
service with a Spring Boot application. Spring Cloud Gateway provides means for routing an incoming request to a 
matching downstream service.

Gateway is a suitable replacement for [Spring Cloud Netflix Zuul](https://spring.io/projects/spring-cloud-netflix) since 
the latter module is now in maintenance mode starting Spring Cloud Greenwich (2.1.0) release train. Spring Cloud will 
continue to support Zuul for a period of at least a year from the general availability of the Greenwich release train. 
Putting a module in the maintenance mode means that the Spring Cloud will no longer add any new feature but will fix 
blocker bugs and security issues.

## Introduction
  - A **route** is the fundamental concept of Spring Cloud Gateway framework. A route contains a destination URL and a
collection of predicates and filters. An request is forwarded to a route if the result of logical _AND_ operation on 
all its predicates is _true_.

  - A **predicate** is boolean valued function.
  
  - A **filter** provides a way of modifying incoming HTTP requests and outgoing HTTP responses.

### A Route Example
Here's a simple example of a route used in this project,

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: book-id
          uri: http://localhost:8080
          predicates:
            - Path=/books/**
          filters:
            - PrefixPath=/public
            - AddRequestHeader=X-Request-Foo, Bar
            - AddRequestTimeHeaderPreFilter
            - AddResponseHeader=X-Response-Bye, Bye
            - AddResponseTimeHeaderPostFilter
```

## Project Synopsis 

This example project consists of two modules:
  - A **book-service** is a Spring Boot based REST service which provides creation and retrieval operation on a book 
  resource. It uses Basic Auth for authentication.
  
  - An **edge-service** is a Spring Cloud Gateway and Spring Boot based Edge service. It routes incoming requests to the 
  backend book service. It uses both Basic Auth and OAuth2 for authenticating a request. Once the request is authenticated, 
  it forwards the request to the book service after replacing the authorization header with book service's basic 
  auth credentials. 
  
Here is the flow of an incoming request and outgoing response in the example edge service.

![](./img/gateway-example-flow-2.png)

### Types of Filter
Gateway filters can be classified into 3 groups:
  
  - **Global Filters**: They are special filters that are conditionally applied to all routes. A good use of a global
    filter can be authentication of an incoming request. Here are the global filters used in this example:
    
    - **Authorization Filter**: A custom filter for authenticating a request. Authentication can be Basic Auth or OAuth2.
    
    - **Basic Auth Token Relay Filter**: A custom filter whi h replaces the authorization header with basic auth credentials 
    specific to a route.
    
  - **Pre Filters**: These filters are applied to incoming requests and are usually specific to a route. Here are the 
  pre filters used in this example:
  
    - **Prefix Filter**: A built-in filter which adds a prefix to the incoming request before being forwarded.
    
    - **Add Request Header Filter**: A built-in filter which adds a header to the incoming request before being forwarded.
    
    - **Add Request Time Header Pre Filter**: A custom filter which add a timestamp header to the incoming request before being forwarded.
  
  - **Post Filters**: These filters are applied to outgoing request and are usually specific to a route. Here are the 
  post filters used in this example:
  
    - **Add Response Header Filter**: A built-in filter which adds a header to the outgoing response.
      
    - **Add Response Time Header Post Filter**: A custom filter which add a timestamp header to the outgoing response.
  
  
## Security
This example didn't use Spring Security framework directly as typically used in a Spring Boot application by configuring the
 service. However, it took advantage of the classes provided in the Spring Security libraries to come up with a custom security
 framework. This example uses both **Basic Authentication** and **OAuth 2.0** for authentication. 
 
### Basic Authentication
The basic authorization credentials are configured using `security.auth.user` and `security.auth.password` in the 
 `application.yml`.
 
### OAuth2 Authentication
The OAuth2 uses `JSON Web Token` (`JWT`) for authentication. The client of the Edge service uses `Client Credentials` 
grant type to obtain an access token from an auth server. In this example, we used Auth0 server and Okta server for 
obtaining an access token.
                                                         
#### Auth0 Authorization Server                                                      
Here's an example to obtain an access token from an `Auth0` server:

```
curl --request POST \
  --url https://ibasak.auth0.com/oauth/token \
  --header 'content-type: application/json' \
  --data '{
	"client_id":"yHJiJecLn3bd8A2oummRa08jp9t0y1UL",
	"client_secret":"YAeV4cYudc2Gyro6sR416-ehmiyxnJc7ErLDwDxNDaBmvGgCu1Y8hyG6Sa-tyfAY",
	"audience":"https://quickstarts/api",
	"grant_type":"client_credentials"
}'
```

A response usually looks something similar to this:

```json
{
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlEwWXlRVGMyTWpGRVJEQXhOa0ZHTVRGQlJqZzJNRVZGTUVFd01UZENOREV6TmpJd1JqTTFOQSJ9.eyJpc3MiOiJodHRwczovL2liYXNhay5hdXRoMC5jb20vIiwic3ViIjoieUhKaUplY0xuM2JkOEEyb3VtbVJhMDhqcDl0MHkxVUxAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vcXVpY2tzdGFydHMvYXBpIiwiaWF0IjoxNTY3Nzg5MDQ2LCJleHAiOjE1Njc4NzU0NDYsImF6cCI6InlISmlKZWNMbjNiZDhBMm91bW1SYTA4anA5dDB5MVVMIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.aFzEvDwsNvUge5yAkzLJfrlpjtxffO2M7V0q0sGF9udi99KVEK3vQ2KXZm_N7v-ASrm-LF7twgPzdiln6tVMWkGtvFmpKx2YQwmXsEDYZGfrHOwb5XjY2AF8eXXsiJQEyI_SOSb-CzoAxFL34eIPeFa77zR6nmcIZAJyCdTtrMd1S4XIENPW1aWvwK5BVqFk6VpJ33LdemQYthQkNMYJF_v8dgXHbqSIAkdOfg4CUKXRObABTc4LnARMiFGFa-c2aQBMj1vP6PRE7h41Fr6MTHkUSVfFFayyVUFI3mH3tfiNHTqQiUZIpNJNknRYCTXDJq2V4mLgWfH9BFjelP65dg",
  "expires_in": 86400,
  "token_type": "Bearer"
}
```

You need to configure the `application.yml` before using the `Auth0` JWT. You have to set the following properties:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://ibasak.auth0.com/
          audience: https://quickstarts/api
```

#### Okta Authorization Server                                                      
Here's an example to obtain an access token from an `Okta` server:

```
curl --request POST \
  --url https://dev-461512.okta.com/oauth2/default/v1/token \
  --header 'accept: application/json' \
  --header 'authorization: Basic MG9hMWFhMXI0MzB3NEFLajczNTc6b0gwOTB0RlZFVG5iV0o4eElQX3BMOXV6SWRmV2h0MWNISjdWb1d0VQ==' \
  --header 'cache-control: no-cache' \
  --header 'content-type: application/x-www-form-urlencoded' \
  --data grant_type=client_credentials \
  --data scope=customScope
```

A response usually looks something similar to this:

```json
{
  "token_type":"Bearer",
  "expires_in":3600,
  "access_token":"eyJraWQiOiItM1N6UVhjWDNYc0lFMmNOSnZ6NGRZRzBZemdXVS1Od091THpxYmZ1cWQ0IiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULkluNHlfNVJQd0N4eHBURkVmVThGRERiSEQyZHF6Q1RiNjBreGxPaDhpMjgiLCJpc3MiOiJodHRwczovL2Rldi00NjE1MTIub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTY3ODA2OTk0LCJleHAiOjE1Njc4MTA1OTQsImNpZCI6IjBvYTFhYTFyNDMwdzRBS2o3MzU3Iiwic2NwIjpbImN1c3RvbVNjb3BlIl0sInN1YiI6IjBvYTFhYTFyNDMwdzRBS2o3MzU3IiwiQ2xhaW0xIjpmYWxzZX0.TShTVtfRp8wU39NY40KpTo1PCLB8N2x3kuVdkgJVYvU5zd5yBkz3RZZLksqsWQEfirAKduBdSkF4aMQhBUo3tdDYefQ6TNqnun_Ung1f3TdUAalyqeUgpGGlbN2J93jv-djtF5O7ylElpKqvwXhZcwXhJb1HPJqLB_LP0XtaxDb5R8uPP56IhE6JEC8PCIvpMOM0gr9mYsJWxwTe-tVd5NHUTSIaDBtMCsFbcx8MkG6YXN0N-B1ZsyZJMHBA8nwWk1Fx7EbIyxTmpUQdnBmwP-YM1XNCvBZQkX9BhId6YnaAjmLhJ_SQB1VWew28oAHpeax9Lkj-R49rzqxsjcTvVA",
  "scope":"customScope"
}
```

You need to configure the `application.yml` before using the `Okta` JWT. You have to set the following properties:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://dev-461512.okta.com/oauth2/default
          audience: api://default
```

[travis-badge]: https://travis-ci.org/indrabasak/spring-gateway-example.svg?branch=master
[travis-badge-url]: https://travis-ci.org/indrabasak/spring-gateway-example/

[sonarqube-badge]: https://sonarcloud.io/api/project_badges/measure?project=com.basaki%3Aspring-gateway-example&metric=alert_status
[sonarqube-badge-url]: https://sonarcloud.io/dashboard/index/com.basaki:spring-gateway-example

[technical-debt-ratio-badge]: https://sonarcloud.io/api/project_badges/measure?project=com.basaki%3Aspring-gateway-example&metric=sqale_index
[technical-debt-ratio-badge-url]: https://sonarcloud.io/dashboard/index/com.basaki:spring-gateway-example

[coverage-badge]: https://sonarcloud.io/api/project_badges/measure?project=com.basaki%3Aspring-gateway-example&metric=coverage
[coverage-badge-url]: https://sonarcloud.io/dashboard/index/com.basaki:spring-gateway-example

