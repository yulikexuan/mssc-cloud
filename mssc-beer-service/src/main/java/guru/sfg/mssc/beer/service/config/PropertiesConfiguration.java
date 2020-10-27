//: guru.sfg.mssc.beer.service.config.PropertiesConfiguration.java


package guru.sfg.mssc.beer.service.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


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