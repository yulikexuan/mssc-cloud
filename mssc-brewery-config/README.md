# MSSC Brewery Config Server

### Set up Spring Cloud Config Clients with Discovery

1.  Add in dependency of Spring Cloud Config Client

    ``` 
    <!-- For Beer-Service, Beer-Inventory-Service, Beer-Order-Service -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    ```

2.  Add bootstrap properties file to Services of Spring Cloud Config

    ``` 
    # For Beer-Service, etc. : bootstrap-local-discovery.yml
    spring:
      cloud:
        discovery:
          enabled: true
        config:
          discovery:
            service-id: mssc-brewery-config
    
    ```

3.  Disable spring.cloud.discovery for local database config file
    
    ``` 
    # For Beer-Service, etc. : bootstrap-local-psql.yml
    spring:
      # Disable Spring Cloud Discovery by Default
      cloud:
        discovery:
          enabled: false
    ```

### Resources