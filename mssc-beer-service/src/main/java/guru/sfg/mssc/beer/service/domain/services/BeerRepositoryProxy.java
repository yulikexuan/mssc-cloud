//: guru.sfg.mssc.beer.service.domain.services.BeerRepositoryProxy.java


package guru.sfg.mssc.beer.service.domain.services;


import guru.sfg.mssc.beer.service.domain.model.Beer;
import guru.sfg.mssc.beer.service.domain.repositories.IBeerRepository;
import guru.sfg.mssc.beer.service.web.model.BeerStyleEnum;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class BeerRepositoryProxy implements IBeerRepositoryProxy {

    @Override
    public Page<Beer> findAllByBeerNameAndBeerStyle(
            @NonNull IBeerRepository beerRepository,
            String beerName, BeerStyleEnum beerStyle,
            @NonNull Pageable pageRequest) {

        final Page<Beer> beerPage;

        if (StringUtils.isNoneBlank(beerName) && Objects.nonNull(beerStyle)) {
            //search both
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(
                    beerName, beerStyle, pageRequest);
        } else if (StringUtils.isNoneBlank(beerName) && Objects.isNull(beerStyle)) {
            //search beer_service name
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (StringUtils.isBlank(beerName) && Objects.nonNull(beerStyle)) {
            //search beer_service style
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        return beerPage;
    }

}///:~