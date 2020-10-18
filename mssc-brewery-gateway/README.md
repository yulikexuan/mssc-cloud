# MSSC Brewery Gateway

### Config Spring Cloud Gateway with Discovery Service

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

### Add in Resilience4J Circuit Breaker

1. Add denpendency

   ``` 
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
    </dependency>
   ```

2. Config the failover route

   ``` 
   - id: beer-inventory-failover
     uri: http://localhost:8084
     predicates:
       - Path=/api/v1/inventory-failover
   ```

3. Config the failover route with discovery

   ``` 
    - id: beer-inventory-failover
      uri: lb://beer-inventory-failover
      predicates:
        - Path=/api/v1/inventory-failover
   ```

4. Set up circuit breaker to inventory service

   ``` 
    - id: beer-inventory-service
      uri: lb://beer-inventory-service
      filters:
        - name: CircuitBreaker
          args:
            name: inventoryCircuitBreaker
            fallbackUri: forward:/api/v1/inventory-failover
      predicates:
        - Path=/api/v1/beer/*/inventory
   ```
### Resources
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway#learn)
- [Writing Custom Spring Cloud Gateway Filters](https://www.baeldung.com/spring-cloud-custom-gateway-filters)
