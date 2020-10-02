//: guru.sfg.beer.order.service.services.beer.BeerService.java


package guru.sfg.beer.order.service.services.beer;


import guru.sfg.beer.order.service.commons.IExecutorServiceFactory;
import guru.sfg.beer.order.service.config.PropertiesConfiguration.SfgBreweryProperties;
import guru.sfg.beer.order.service.web.model.BeerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;


@Service
public class BeerService implements IBeerService {

    public final static String BEER_PATH_V1 = "/api/v1/beer/";
    public final static String BEER_UPC_PATH_V1 = "/api/v1/upcbeer/";

    private final RestTemplate restTemplate;
    private final String beerServiceHost;
    private final ExecutorService executorService;

    @Autowired
    public BeerService(RestTemplateBuilder restTemplateBuilder,
                       SfgBreweryProperties sfgBreweryProperties,
                       IExecutorServiceFactory executorServiceFactory) {

        this.restTemplate = restTemplateBuilder.build();
        this.beerServiceHost = sfgBreweryProperties.getBeerServiceHost();
        this.executorService = executorServiceFactory.newExitingExecutorService(
                10, 10, 1000L,
                1000);
    }

    @Override
    public CompletableFuture<Optional<BeerDto>> getBeerById(final UUID beerId) {
        return CompletableFuture.supplyAsync(
                        () -> this.getBeer(() -> this.getUrlWithId(beerId)),
                        this.executorService)
                .exceptionally(e -> Optional.empty());
    }

    @Override
    public CompletableFuture<Optional<BeerDto>> getBeerByUpc(final String upc) {
        return CompletableFuture.supplyAsync(
                () -> this.getBeer(() -> this.getUrlWithUpc(upc)),
                this.executorService)
                .exceptionally(e -> Optional.empty());
    }

    private Optional<BeerDto> getBeer(Supplier<String> apiSupplier) {
        return Optional.ofNullable(restTemplate.getForObject(apiSupplier.get(),
                BeerDto.class));
    }

    private String getUrlWithId(UUID beerId) {
        return this.beerServiceHost + BEER_PATH_V1 + beerId.toString();
    }

    private String getUrlWithUpc(String upc) {
        return this.beerServiceHost + BEER_UPC_PATH_V1 + upc;
    }

}///:~