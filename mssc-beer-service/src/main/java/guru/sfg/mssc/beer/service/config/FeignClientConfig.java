//: guru.sfg.mssc.beer.service.config.FeignClientConfig.java


package guru.sfg.mssc.beer.service.config;


import feign.auth.BasicAuthRequestInterceptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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