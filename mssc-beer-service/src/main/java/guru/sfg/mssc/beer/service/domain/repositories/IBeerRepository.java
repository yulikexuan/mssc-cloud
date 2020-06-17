//: guru.sfg.mssc.beer.service.domain.repositories.IBeerRepository.java


package guru.sfg.mssc.beer.service.domain.repositories;


import guru.sfg.mssc.beer.service.domain.model.Beer;
import guru.sfg.mssc.beer.service.web.model.BeerStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/*
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 */
@Repository
public interface IBeerRepository extends JpaRepository<Beer, UUID> {

    Page<Beer> findAllByBeerName(String beerName, Pageable pageable);

    Page<Beer> findAllByBeerStyle(BeerStyleEnum beerStyle, Pageable pageable);

    Page<Beer> findAllByBeerNameAndBeerStyle(
            String beerName, BeerStyleEnum beerStyle, Pageable pageable);

    Beer findByUpc(String upc);

}///:~