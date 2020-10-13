//: guru.sfg.brewery.gateway.config.GoogleConfig.java


package guru.sfg.brewery.gateway.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("google")
@Configuration
public class GoogleConfig {

    @Bean("googleRouteConfig")
    RouteLocator googleConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(path -> path.path("/googlesearch2")
                        .filters(filter -> filter.rewritePath(
                                "/googlesearch2(?<segment>/?.*)",
                                "/${segment}"))
                        .uri("https://google.com/"))
                .build();
    }

}///:~