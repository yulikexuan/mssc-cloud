//: guru.sfg.mssc.beer.service.domain.services.BeerService.java


package guru.sfg.mssc.beer.service.domain.services;


import guru.sfg.mssc.beer.service.domain.model.Beer;
import guru.sfg.mssc.beer.service.domain.repositories.IBeerRepository;
import guru.sfg.mssc.beer.service.web.controller.NotFoundException;
import guru.sfg.mssc.beer.service.web.mapper.IBeerMapper;
import guru.sfg.mssc.beer.service.web.model.BeerDto;
import guru.sfg.mssc.beer.service.web.model.BeerPagedList;
import guru.sfg.mssc.beer.service.web.model.BeerStyleEnum;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service
public class BeerService implements IBeerService {

    private final IBeerRepository beerRepository;
    private final IBeerMapper beerMapper;

    @Autowired
    public BeerService(IBeerRepository beerRepository, IBeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Override
    public BeerPagedList listBeers(
            String beerName, BeerStyleEnum beerStyle,
            PageRequest pageRequest, Boolean showInventoryOnHand) {

        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

        if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search both
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(
                    beerName, beerStyle, pageRequest);
        } else if (!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle)) {
            //search beer_service name
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search beer_service style
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventoryOnHand){
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDtoWithInventory)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(beerPage.getPageable().getPageNumber(),
                                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        } else {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(beerPage.getPageable().getPageNumber(),
                                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        }

        return beerPagedList;
    }

    @Override
    public BeerDto getById(@NonNull UUID id) {

        Beer beer = this.findBeerById(id);
        BeerDto beerDto = this.beerMapper.beerToBeerDto(beer);

        return beerDto;
    }

    @Override
    public BeerDto saveNewBeer(@NonNull BeerDto beerDto) {

        Beer beer = this.beerMapper.beerDtoToBeer(beerDto);
        BeerDto savedBeerDto = this.beerMapper.beerToBeerDto(
                this.beerRepository.save(beer));

        return savedBeerDto;
    }

    @Override
    public BeerDto updateBeer(@NonNull UUID id, @NonNull BeerDto beerDto) {

        Beer beer = this.findBeerById(id);

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return this.beerMapper.beerToBeerDto(beer);
    }

    private Beer findBeerById(@NonNull UUID id) {
        return this.beerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("There is not Beer with ID %s",
                                id.toString())));
    }

}///:~