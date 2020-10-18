//: guru.sfg.brewery.inventoryfailover.config.RouterConfig.java


package guru.sfg.brewery.inventoryfailover.config;


import guru.sfg.brewery.inventoryfailover.web.BeerInventoryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class RouterConfig {

    static final String INVENTORY_FAILOVER_PATH = "/api/v1/inventory-failover";

    @Bean
    public RouterFunction inventoryRoute(BeerInventoryHandler inventoryHandler) {
        return route(GET(INVENTORY_FAILOVER_PATH)
                .and(accept(MediaType.APPLICATION_JSON)),
                inventoryHandler::listInventory);
    }


}///:~