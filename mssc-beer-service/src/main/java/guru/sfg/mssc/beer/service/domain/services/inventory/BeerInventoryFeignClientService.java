//: guru.sfg.mssc.beer.service.domain.services.inventory.BeerInventoryFeignClientService.java


package guru.sfg.mssc.beer.service.domain.services.inventory;


import guru.sfg.mssc.beer.service.web.model.inventory.BeerInventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Slf4j
@Service
@Profile("local-discovery")
@RequiredArgsConstructor
public class BeerInventoryFeignClientService implements IBeerInventoryService {

    private final IBeerInventoryFeignClient beerInventoryFeignClient;

    @Override
    public Integer getOnhandInventory(UUID beerId) {

        log.debug(">>>>>>> Calling Inventory Service with OpenFeign");

        ResponseEntity<List<BeerInventoryDto>> responseEntity =
                beerInventoryFeignClient.getOnHandInventory(beerId);

        //sum from inventory list
        int sum = Objects.requireNonNull(responseEntity.getBody())
                .stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();

        log.debug(">>>>>>> Beer inventory {} on hand is {}", beerId, sum);

        return sum;
    }

}///:~