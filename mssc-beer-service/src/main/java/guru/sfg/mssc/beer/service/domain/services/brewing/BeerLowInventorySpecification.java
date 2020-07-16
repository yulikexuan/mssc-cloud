//: guru.sfg.mssc.beer.service.domain.services.brewing.BeerLowInventorySpecification.java


package guru.sfg.mssc.beer.service.domain.services.brewing;


import guru.sfg.mssc.beer.service.domain.model.Beer;
import guru.sfg.mssc.beer.service.domain.services.IBeerSpecification;
import guru.sfg.mssc.beer.service.domain.services.inventory.IBeerInventoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class BeerLowInventorySpecification implements IBeerSpecification {

    private final IBeerInventoryService beerInventoryService;

    @Override
    public boolean isSatisfied(@NonNull Beer beer) {

        int inventoryOnHand = this.beerInventoryService.getOnhandInventory(
                beer.getId());
        int minOnHand = beer.getMinOnHand();

        log.info(">>>>>>> The min on-hand should be: {}", minOnHand);
        log.info(">>>>>>> The inventory on-hand is {} now.", inventoryOnHand);

        return minOnHand >= inventoryOnHand;
    }

}///:~