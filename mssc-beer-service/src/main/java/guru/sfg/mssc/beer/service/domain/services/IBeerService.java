//: guru.sfg.mssc.beer.service.domain.services.IBeerService.java


package guru.sfg.mssc.beer.service.domain.services;


import com.google.common.collect.ImmutableList;
import guru.sfg.mssc.beer.service.domain.model.Beer;
import guru.sfg.mssc.beer.service.web.mapper.IBeerMapper;
import guru.sfg.mssc.beer.service.web.model.BeerDto;
import guru.sfg.mssc.beer.service.web.model.BeerPagedList;
import guru.sfg.mssc.beer.service.web.model.BeerStyleEnum;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public interface IBeerService {

    BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle,
                            PageRequest pageRequest, Boolean showInventoryOnHand);

    BeerDto getById(UUID id);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID id, BeerDto beerDto);

}///:~