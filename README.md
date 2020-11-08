# MSSC Cloud for Beer
The Spring Cloud Lab

## The MSSC Brewery Services

### Microservice List

- beer-service
  - port: 8081

- beer-inventory-service
  - port: 8083

- beer-order-service
  - port: 8082

- beer-inventory-failover
  - port: 8084

### Spring Cloud Service List

- mssc-brewery-gateway
  - port: 9090

- mssc-brewery-eureka
  - port: 8761

- mssc-brewery-config
  - port: 8888
  
  
### The API

#### Get Beer By Id with Inventory Data

curl -s -w '\n\nTotal: %{time_total} Seconds\n' http://localhost:8081/api/v1/beer/026cc3c8-3a0c-4083-a05b-e908048c1b08?showInventoryOnHand=true

#### Get Beer By UPC with Inventory Data

curl -s -w '\n\nTotal: %{time_total} Seconds\n' http://localhost:8081/api/v1/upcbeer/0631234200036?showInventoryOnHand=true

#### Get Beer By Id without Inventory Data

curl -s -w '\n\nTotal: %{time_total} Seconds\n' http://localhost:8081/api/v1/beer/026cc3c8-3a0c-4083-a05b-e908048c1b08

#### Get Beer By UPC without Inventory Data

curl -s -w '\n\nTotal: %{time_total} Seconds\n' http://localhost:8081/api/v1/upcbeer/0071990316006

### The Database

- psql -h localhost -U postgres

- How Many Database Connections created in DB Server
    - `SELECT sum(numbackends) FROM pg_stat_database;`

- List all users
    - ` \du `

- Connect to database with psql
    - ` psql -h localhost -U username databasename `

- List all database
    - ` \l `

- Connect to a specific database
    - ` \c <datebase_name> `

- List all schemas
    - ` \dn `

- List all tables
    - ` \dt `
    - ` \dt+ `
- Get detailed information on a table
    - ` \d+ <table name> `

- To quit from psql
    - ` postgres=# \q `

### ActiveMQ-Artemis in Docker Container
- user: artemis
- password: simetraehcapa
- For Windows 10: 
  - Start ActiveMQ-Artemis
    - ``` winpty docker run -it --rm -d -p 8161:8161 -p 61616:61616 vromero/activemq-artemis ```
  - Stop ActiveMQ-Artemis
    - ``` docker stop <container name> ```

- For Linux: 
    - Start ActiveMQ-Artemis
      - ``` docker run -it --rm -d -p 8161:8161 -p 61616:61616 vromero/activemq-artemis ```
  - Stop ActiveMQ-Artemis
    - ``` docker stop <container name> ```

### Zipkin Server in Docker Container
- For Windows 10: 
  - ``` winpty docker run -d -p 9411:9411 openzipkin/zipkin ```
  
### How to Secure Inventory Service with BasicAuth

1.  Add ```username``` and ```password``` to ```mssc-config-repo```

    ``` 
    spring:
      security:
        user:
          name: inventory
          password: "{cipher}f565b94774ad31bbf6c817ea5b1db1595582a00745c40b9f687bafd281a62933"
    ```

2.  Add maven dependency

    ``` 
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    ```

3.  Create configuration class for BasicAuth

    ``` 
    @Slf4j
    @Configuration
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic();
        }
    
    }///:~
    ```

4.  Config client service of Inventory Service: Beer Service

    - Add credential of Inventory Service to ``` application-local-secure.yml ``` of ``` mssc-config-repo ```
    
      ``` 
        sfg:
          brewery:
            beer-inventory-service-host: http://localhost:8083
            beer-inventory-user-name: inventory
            beer-inventory-password: "{cipher}f565b94774ad31bbf6c817ea5b1db1595582a00745c40b9f687bafd281a62933"
      ```
      
    - Add credential to ``` PropertiesConfiguration ```
    
      ``` 
        @Configuration
        public class PropertiesConfiguration {
        
            @Bean
            @ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = true)
            public SfgBreweryProperties sfgBrewery() {
                return new SfgBreweryProperties();
            }
        
            @Getter
            @Setter
            public static class SfgBreweryProperties {
        
                @NotBlank
                private String beerInventoryServiceHost;
        
                @NotBlank
                private String hostName;
        
                @Min(1025)
                @Max(8080)
                private int port;
        
                @NotBlank
                private String beerInventoryUserName;
        
                @NotBlank
                private String beerInventoryPassword;
            }
        
        }///:~
      ```
      
    - Config ``` RestTemplate ``` to use ``` BasicAuth ``` Credential
    
      ``` 
      public class BeerInventoryServiceRestTemplate implements IBeerInventoryService {
      
          public static final String INVENTORY_PATH = "/api/v1/beer/{beerId}/inventory";
      
          private final RestTemplate restTemplate;
          private final String inventoryUserName;
          private final String inventoryPassword;
          private final String beerInventoryServiceHost;
      
      
          @Autowired
          public BeerInventoryServiceRestTemplate(
                  PropertiesConfiguration.SfgBreweryProperties sfgBreweryProperties,
                  RestTemplateBuilder restTemplateBuilder) {
      
              this.beerInventoryServiceHost =
                      sfgBreweryProperties.getBeerInventoryServiceHost();
              this.inventoryUserName = sfgBreweryProperties.getBeerInventoryUserName();
              this.inventoryPassword = sfgBreweryProperties.getBeerInventoryPassword();
      
              this.restTemplate = restTemplateBuilder
                      .basicAuthentication(inventoryUserName, inventoryPassword)
                      .build();
          }
          ... ...
      }
      ``` 
      
    - Add configuration class for ``` FeignClient ```
    
      ``` 
        @Slf4j
        @Configuration
        @AllArgsConstructor
        public class FeignClientConfig {
        
            private final PropertiesConfiguration.SfgBreweryProperties sfgBreweryProperties;
        
            @Bean
            public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
                return new BasicAuthRequestInterceptor(
                        sfgBreweryProperties.getBeerInventoryUserName(),
                        sfgBreweryProperties.getBeerInventoryPassword());
            }
        
        }///:~
      ```
      
    - Apply ``` FeignClientConfig ``` to ``` IBeerInventoryFeignClient ```
    
      ``` 
        @FeignClient(name = "beer-inventory-service",
                fallback = BeerInventoryFailoverFeignClientService.class,
                configuration = FeignClientConfig.class)
        public interface IBeerInventoryFeignClient {
        
            String INVENTORY_PATH = BeerInventoryServiceRestTemplate.INVENTORY_PATH;
        
            @GetMapping(value = INVENTORY_PATH)
            ResponseEntity<List<BeerInventoryDto>> getOnHandInventory(
                    @PathVariable UUID beerId);
        
        }///:~
      ```


### Solution for Tricky Issues

#### Hibernate could not initialize proxy – no Session

### Run in Docker

- [Building Docker Images with Maven](README_Docker.md)



### Resources
* [psql cheatsheet](https://www.postgresqltutorial.com/postgresql-cheat-sheet/)
* [cURL - Write Out](https://ec.haxx.se/usingcurl/usingcurl-verbose/usingcurl-writeout)
* [cURL - Measure Times](https://stackoverflow.com/questions/18215389/how-do-i-measure-request-and-response-times-at-once-using-curl)
* [Docker-for-Windows & Hyper-V excluding but not using important port ranges](https://github.com/docker/for-win/issues/3171)
* [Hands-On Guide to Spring Cloud Contract](https://learning.oreilly.com/videos/hands-on-guide-to/9780135598436)
