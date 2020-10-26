# MSSC Brewery Eureka Server

### Secure Eureka Server

1.  Add security dependencies

    ``` 
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-rsa</artifactId>
    </dependency>
    ```
    
2.  Add ``` username ``` and ``` password ``` to ``` application.yml ```

    ``` 
    spring:
      security:
        user:
          name: yulikexuan
          password: "{cipher}f565b94774ad31bbf6c817ea5b1db1595582a00745c40b9f687bafd281a62933"
    ```

3.  Add ``` bootstrap.yml ``` to ``` resources ``` folder

    ``` 
    encrypt:
      key: supersecretkey
    ```

4.  Add class ``` guru.sfg.brewery.eureka.config.SecurityConfig ```

    ```
    @Slf4j
    @Configuration
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
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

5.  Config Eureka's client using Basic Auth to access Eureka

    - Add Eureka's ``` username ``` and ``` password ``` to ``` bootstrap.yml ```

      ``` 
        eureka:
          client:
            service-url:
              defaultZone: "http://yulikexuan:password@localhost:8761/eureka"
      ```



### Resources
- [Spring Cloud Netflix](https://spring.io/projects/spring-cloud-netflix)
- [Reference Doc](https://docs.spring.io/spring-cloud-netflix/docs/current/reference/html/)
- [Introduction to Spring Cloud Netflix – Eureka](https://www.baeldung.com/spring-cloud-netflix-eureka)