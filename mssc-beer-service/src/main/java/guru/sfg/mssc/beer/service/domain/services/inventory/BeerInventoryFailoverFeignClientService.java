//: guru.sfg.mssc.beer.service.domain.services.inventory.BeerInventoryFailoverFeignClientService.java


package guru.sfg.mssc.beer.service.domain.services.inventory;


import guru.sfg.mssc.beer.service.web.model.inventory.BeerInventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class BeerInventoryFailoverFeignClientService
        implements IBeerInventoryFeignClient {

    private final IBeerInventoryFailoverFeignClient failoverFeignClient;

    @Override
    public ResponseEntity<List<BeerInventoryDto>> getOnHandInventory(
            @PathVariable UUID beerId) {

        return this.failoverFeignClient.getOnHandInventory();
    }

}///:~