//: guru.sfg.brewery.gateway.config.FirstPreLastPostGlobalFilter.java


package guru.sfg.brewery.gateway.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class FirstPreLastPostGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        log.debug(">>>>>>> First Pre-GlobalFilter {}",
                exchange.getRequest().getURI());

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    log.debug(">>>>>>> Last Post-GlobalFilter {}",
                            exchange.getRequest().getURI());
                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }

}///:~