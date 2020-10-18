# MSSC Brewery Gateway

## Config Spring Cloud Gateway with Discovery Service

1. Add dependency

   ``` 
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
   ```
2. Config ```application.yml```
   ``` 
    spring:
      cloud:
        loadbalancer:
          ribbon:
            enabled: false
        gateway:
          routes:
            - id: beer-service
              uri: lb://beer-service
              predicates:
                - Path=/api/v1/beer*,/api/v1/beer/*,/api/v1/upcbeer*,/api/v1/upcbeer/*
            - id: beer-order-service
              uri: lb://beer-order-service
              predicates:
                - Path=/api/v1/customers*,/api/v1/customers/**
            - id: beer-inventory-service
              uri: lb://beer-inventory-service
              predicates:
                - Path=/api/v1/beer/*/inventory
    logging:
      level:
        org.springframework.cloud.gateway: DEBUG
   ```

## Resources
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway#learn)
- [Writing Custom Spring Cloud Gateway Filters](https://www.baeldung.com/spring-cloud-custom-gateway-filters)
