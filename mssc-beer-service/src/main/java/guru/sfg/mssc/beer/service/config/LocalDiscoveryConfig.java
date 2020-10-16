//: guru.sfg.mssc.beer.service.config.LocalDiscoveryConfig.java


package guru.sfg.mssc.beer.service.config;


import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("local-discovery")
@Configuration
@EnableDiscoveryClient
public class LocalDiscoveryConfig {

}///:~