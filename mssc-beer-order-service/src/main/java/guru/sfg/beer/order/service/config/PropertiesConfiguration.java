//: guru.sfg.beer.order.service.config.PropertiesConfiguration.java


package guru.sfg.beer.order.service.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;


@Configuration
public class PropertiesConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
    public SfgBreweryProperties sfgBreweryProperties() {
        return new SfgBreweryProperties();
    }

    @Getter
    @Setter
    public static class SfgBreweryProperties {

        SfgBreweryProperties() {}

        @NotBlank
        private String beerServiceHost;
    }

}///:~