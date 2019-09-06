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
  
  - **Pre Filters**: These filters are applied to incoming requests.
  
  - **Post Filters**: These filters are applied to outgoing request.
  
  - **Global Filters**: They are special filters that are conditionally applied to all routes. A good use of a global
  filter can be authentication of an incoming request.  
  
## Security
This example didn't use Spring security framework as typically used in a Spring Boot application but took advantage of 
the classes provided in the Spring security libraries.

This example uses both **Basic Authentication** and **OAuth 2.0** for authenticating a request. 

[travis-badge]: https://travis-ci.org/indrabasak/spring-gateway-example.svg?branch=master
[travis-badge-url]: https://travis-ci.org/indrabasak/spring-gateway-example/

[sonarqube-badge]: https://sonarcloud.io/api/project_badges/measure?project=com.basaki%3Aspring-gateway-example&metric=alert_status
[sonarqube-badge-url]: https://sonarcloud.io/dashboard/index/com.basaki:spring-gateway-example

[technical-debt-ratio-badge]: https://sonarcloud.io/api/project_badges/measure?project=com.basaki%3Aspring-gateway-example&metric=sqale_index
[technical-debt-ratio-badge-url]: https://sonarcloud.io/dashboard/index/com.basaki:spring-gateway-example

[coverage-badge]: https://sonarcloud.io/api/project_badges/measure?project=com.basaki%3Aspring-gateway-example&metric=coverage
[coverage-badge-url]: https://sonarcloud.io/dashboard/index/com.basaki:spring-gateway-example

