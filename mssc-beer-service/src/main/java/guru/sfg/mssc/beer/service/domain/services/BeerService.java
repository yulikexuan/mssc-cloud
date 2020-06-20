//: guru.sfg.mssc.beer.service.domain.services.BeerService.java


package guru.sfg.mssc.beer.service.domain.services;


import com.google.common.collect.ImmutableList;
import guru.sfg.mssc.beer.service.commons.IExecutorServiceFactory;
import guru.sfg.mssc.beer.service.domain.model.Beer;
import guru.sfg.mssc.beer.service.domain.repositories.IBeerRepository;
import guru.sfg.mssc.beer.service.web.controller.NotFoundException;
import guru.sfg.mssc.beer.service.web.mapper.IBeerMapper;
import guru.sfg.mssc.beer.service.web.model.BeerDto;
import guru.sfg.mssc.beer.service.web.model.BeerPagedList;
import guru.sfg.mssc.beer.service.web.model.BeerStyleEnum;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Slf4j
@Service
public class BeerService implements IBeerService {

    private final IBeerRepository beerRepository;
    private final IBeerRepositoryProxy beerRepositoryProxy;
    private final IBeerMapper beerMapper;
    private final ExecutorService executorService;

    private final Map<Boolean, Function<Page<Beer>, BeerPagedList>> pageToDtoFuncs;

    @Autowired
    public BeerService(IBeerRepository beerRepository,
                       IBeerMapper beerMapper,
                       IBeerRepositoryProxy beerRepositoryProxy,
                       IExecutorServiceFactory executorServiceFactory) {

        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
        this.beerRepositoryProxy = beerRepositoryProxy;

        this.executorService = executorServiceFactory.newExitingExecutorService(
                5, 5, 1000L,
                1000);

        this.pageToDtoFuncs = Map.of(
                true, this::getBeerDtoWithInventoryFromBeerPage,
                false, this::getBeerDtoFromBeerPage);
    }

    @Override
    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle,
            PageRequest pageRequest, Boolean showInventoryOnHand) {

        log.info(">>>>>>> [API - List Beers] was called.");

        BeerPagedList beerPagedList;

        Page<Beer> beerPage = this.beerRepositoryProxy.findAllByBeerNameAndBeerStyle(
                this.beerRepository, beerName, beerStyle, pageRequest);

        Function<Page<Beer>, BeerPagedList> mappingFunc =
                this.pageToDtoFuncs.get(showInventoryOnHand);

        beerPagedList = mappingFunc.apply(beerPage);

        return beerPagedList;
    }

    @Override
    @Cacheable(cacheNames = "beerCache", key = "#beerId",
            condition = "#showInventoryOnHand == false")
    public BeerDto getById(@NonNull UUID beerId, boolean showInventoryOnHand) {
        log.info(">>>>>>> [API - Get Beer by ID] was called.");
        Beer beer = this.findBeerById(beerId);
        return this.mapBeerToDto(beer, showInventoryOnHand);
    }

    @Override
    @Cacheable(cacheNames = "beerUpcCache", key = "#upc",
            condition = "#showInventoryOnHand == false")
    public BeerDto getByUpc(@NonNull String upc, boolean showInventoryOnHand) {
        log.info(">>>>>>> [API - Get Beer by UPC] was called.");
        Beer beer = this.findBeerByUpc(upc);
        return this.mapBeerToDto(beer, showInventoryOnHand);
    }

    private BeerDto mapBeerToDto(@NonNull Beer beer,
                                 @NonNull boolean showInventoryOnHand) {

        return showInventoryOnHand ?
                this.mapBeerToBeerDtoWithInventory(beer).join() :
                this.beerMapper.beerToBeerDto(beer);
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

    BeerPagedList getBeerDtoFromBeerPage(@NonNull Page<Beer> beerPage) {

        List<BeerDto> beerDtoList = beerPage.getContent().stream()
                .map(beerMapper::beerToBeerDto)
                .collect(ImmutableList.toImmutableList());

        Pageable srcPageable = beerPage.getPageable();
        Pageable pageable = PageRequest.of(srcPageable.getPageNumber(),
                srcPageable.getPageSize());

        long total = beerPage.getTotalElements();

        return new BeerPagedList(beerDtoList, pageable, total);
    }

    BeerPagedList getBeerDtoWithInventoryFromBeerPage(@NonNull Page<Beer> beerPage) {

        List<CompletableFuture<BeerDto>> beerDtoFutures =
                beerPage.getContent().stream()
                        .map(this::mapBeerToBeerDtoWithInventory)
                        .collect(ImmutableList.toImmutableList());

        List<BeerDto> beerDtoList = beerDtoFutures.stream()
                .map(CompletableFuture::join)
                .collect(ImmutableList.toImmutableList());

        Pageable srcPageable = beerPage.getPageable();
        Pageable pageable = PageRequest.of(srcPageable.getPageNumber(),
                srcPageable.getPageSize());

        long total = beerPage.getTotalElements();

        return new BeerPagedList(beerDtoList, pageable, total);
    }

    private Beer findBeerById(@NonNull UUID id) {
        return this.beerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("There is not Beer with ID %s",
                                id.toString())));
    }

    private Beer findBeerByUpc(@NonNull String upc) {
        return this.beerRepository.findByUpc(upc)
                .orElseThrow(() -> new NotFoundException(
                        String.format("There is no Beer with UPC %s", upc)));

    }

    private CompletableFuture<BeerDto> mapBeerToBeerDtoWithInventory(Beer beer) {
        return CompletableFuture
                .supplyAsync(() -> this.beerMapper.beerToBeerDtoWithInventory(beer),
                        this.executorService)
                .exceptionally(e -> this.beerMapper.beerToBeerDto(beer));
    }

}///:~