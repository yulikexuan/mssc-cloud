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

### Encrypt & Decrypt Secure Data

1.  Config Server Setup

    - Add bootstrap.yml to resources folder
    - Add 
      ``` 
      encrypt:
        key: supersecretkey
      ```
      to ``` bootstrap.yml ```
      
2.  Post ``` http://localhost:8888/encrypt ``` with data in the body for encrypting

3.  Post ``` http://localhost:8888/decrypt ``` with data in the body for decrypting

4.  Make Config Server not decrypt encrypted data for clients

    - Add 
      ``` 
      spring:
        cloud:
          config:
            server:
              encrypt:
                enabled: false
      ```
      to ``` bootstrap.yml ```

    - Config Client Services of Config Server
      - Add 
        ``` 
        encrypt:
          key: supersecretkey
        ```
        to ``` application-local-discovery.yml ``` and make sure the key is same as the one of Config Server
      - Add 
        ``` 
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-rsa</artifactId>
        </dependency>
        ```
        to ``` pom.xml ``` of the client service
      - Create new configuration file for secure data, for example, 
        - Add new ``` application-local-secure.yml ``` file
        - Add encrypted data into it: 
          ``` 
          spring:
            datasource:
              url: jdbc:postgresql://localhost:5432/beerservice?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
              username: beer_service_guru
              password: "{cipher}f565b94774ad31bbf6c817ea5b1db1595582a00745c40b9f687bafd281a62933"
            artemis:
              user: artemis
              password: "{cipher}cb06ee1d8b39b83855b818d671094aa53f2bcf667f1ece939a06170235eb350e"
          ```
        

### Resources
- [Spring Cloud Config – Encrypt / Decrypt Configuration Properties](https://asbnotebook.com/2019/08/06/spring-cloud-config-encrypt-decrypt-configuration-properties/)