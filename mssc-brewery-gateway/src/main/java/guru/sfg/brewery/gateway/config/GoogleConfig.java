//: guru.sfg.brewery.gateway.config.GoogleConfig.java


package guru.sfg.brewery.gateway.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Slf4j
@Profile("google")
@Configuration
public class GoogleConfig {

    @Bean("googleRouteConfig")
    RouteLocator googleConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(path -> path.path("/googlesearch")
                        .filters(filter -> filter.rewritePath(
                                "/googlesearch(?<segment>/?.*)",
                                "/${segment}"))
                        .uri("https://google.com/"))
                .build();
    }

}///:~