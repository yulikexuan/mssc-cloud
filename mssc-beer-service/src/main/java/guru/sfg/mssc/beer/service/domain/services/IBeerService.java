//: guru.sfg.mssc.beer.service.domain.services.IBeerService.java


package guru.sfg.mssc.beer.service.domain.services;


import guru.sfg.mssc.beer.service.web.model.BeerDto;
import guru.sfg.mssc.beer.service.web.model.BeerPagedList;
import guru.sfg.mssc.beer.service.web.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;


public interface IBeerService {

    BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle,
                            PageRequest pageRequest, Boolean showInventoryOnHand);

    BeerDto getById(UUID id, boolean showInventoryOnHand);

    BeerDto getByUpc(String upc, boolean showInventoryOnHand);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID id, BeerDto beerDto);

    int getQuantityToBrew(UUID beerId);

}///:~