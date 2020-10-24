//: guru.sfg.brewery.inventoryfailover.web.BeerInventoryHandler.java


package guru.sfg.brewery.inventoryfailover.web;


import guru.sfg.brewery.inventoryfailover.model.BeerInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Slf4j
@Component
public class BeerInventoryHandler {

    public Mono<ServerResponse> listInventory(ServerRequest request) {

        log.debug(">>>>>>> Failover a list of inventory.");

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(Mono.just(failoverBeerInventoryDtoList()),
                        BeerInventoryDto.class);
    }

    private List<BeerInventoryDto> failoverBeerInventoryDtoList() {
        return List.of(BeerInventoryDto.builder()
                .id(UUID.randomUUID())
                .beerId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .quantityOnHand(999)
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .build());
    }

}///:~