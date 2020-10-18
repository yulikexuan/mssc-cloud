//: guru.sfg.mssc.beer.service.domain.services.inventory.IBeerInventoryFailoverFeignClient.java


package guru.sfg.mssc.beer.service.domain.services.inventory;


import guru.sfg.mssc.beer.service.web.model.inventory.BeerInventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "beer-inventory-failover")
public interface IBeerInventoryFailoverFeignClient {

    String INVENTORY_PATH = "/api/v1/inventory-failover";

    @GetMapping(value = INVENTORY_PATH)
    ResponseEntity<List<BeerInventoryDto>> getOnHandInventory();

}///:~