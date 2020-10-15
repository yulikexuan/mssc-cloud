//: guru.sfg.brewery.gateway.config.LocalHostRouteConfig.java


package guru.sfg.brewery.gateway.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@Slf4j
// @Configuration
public class LocalHostRouteConfig {

    static final String[] BEER_SERVICE_PATHS = {
            "/api/v1/beer*",
            "/api/v1/beer/*",
            "/api/v1/upcbeer*",
            "/api/v1/upcbeer/*",
    };

    static final String BEER_SERVICE_URI = "http://localhost:8081";

    static final String BEER_SERVICE_ID = "beer-service";

    @Bean("googleRouteConfig")
    RouteLocator googleConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(route -> route.path(BEER_SERVICE_PATHS)
                        .uri(BEER_SERVICE_URI)
                        .id(BEER_SERVICE_ID))
                .build();
    }

}///:~