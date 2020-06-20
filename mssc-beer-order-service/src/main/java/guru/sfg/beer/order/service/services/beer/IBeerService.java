//: guru.sfg.beer.order.service.services.beer.IBeerService.java


package guru.sfg.beer.order.service.services.beer;


import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import guru.sfg.beer.order.service.web.model.BeerDto;


public interface IBeerService {

    CompletableFuture<Optional<BeerDto>> getBeerById(UUID uuid);

    CompletableFuture<Optional<BeerDto>> getBeerByUpc(String upc);

}///:~