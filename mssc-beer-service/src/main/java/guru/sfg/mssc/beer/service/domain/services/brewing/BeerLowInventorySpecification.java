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

        boolean shouldBrew = minOnHand >= inventoryOnHand;

        if (shouldBrew) {
            log.info(">>>>>>> The quantity [min / inventory : {} / {}] : " +
                            "Sending brewing event ... ...",
                    minOnHand, inventoryOnHand);
        }

        return shouldBrew;
    }

}///:~