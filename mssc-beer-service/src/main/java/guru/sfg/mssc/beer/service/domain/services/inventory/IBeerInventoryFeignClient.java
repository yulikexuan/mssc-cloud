//: guru.sfg.mssc.beer.service.domain.services.inventory.IBeerInventoryFeignClient.java


package guru.sfg.mssc.beer.service.domain.services.inventory;


import guru.sfg.mssc.beer.service.config.FeignClientConfig;
import guru.sfg.mssc.beer.service.web.model.inventory.BeerInventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;


@FeignClient(name = "beer-inventory-service",
        fallback = BeerInventoryFailoverFeignClientService.class,
        configuration = FeignClientConfig.class)
public interface IBeerInventoryFeignClient {

    String INVENTORY_PATH = BeerInventoryServiceRestTemplate.INVENTORY_PATH;

    @GetMapping(value = INVENTORY_PATH)
    ResponseEntity<List<BeerInventoryDto>> getOnHandInventory(
            @PathVariable UUID beerId);

}///:~