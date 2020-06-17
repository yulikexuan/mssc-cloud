//: guru.sfg.mssc.beer.service.domain.services.IBeerRepositoryProxy.java


package guru.sfg.mssc.beer.service.domain.services;


import guru.sfg.mssc.beer.service.domain.model.Beer;
import guru.sfg.mssc.beer.service.domain.repositories.IBeerRepository;
import guru.sfg.mssc.beer.service.web.model.BeerStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IBeerRepositoryProxy {

    Page<Beer> findAllByBeerNameAndBeerStyle(
            IBeerRepository beerRepository,
            String beerName,
            BeerStyleEnum beerStyle,
            Pageable pageRequest);

}///:~