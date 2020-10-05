//: guru.sfg.beer.order.service.statemachine.IBeerOrderManager.java


package guru.sfg.beer.order.service.statemachine;


import guru.sfg.beer.order.service.domain.BeerOrder;

import java.util.Optional;
import java.util.UUID;


public interface IBeerOrderManager {

    Optional<UUID> newBeerOrder(BeerOrder beerOrder);

    void beerOrderPickedUp(UUID beerOrderId);

}///:~