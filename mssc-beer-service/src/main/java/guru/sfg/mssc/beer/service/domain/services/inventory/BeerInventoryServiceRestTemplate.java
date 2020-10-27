//: guru.sfg.mssc.beer.service.domain.services.inventory.BeerInventoryServiceRestTemplate.java


package guru.sfg.mssc.beer.service.domain.services.inventory;


import guru.sfg.mssc.beer.service.config.PropertiesConfiguration;
import guru.sfg.mssc.beer.service.web.controller.NotFoundException;
import guru.sfg.mssc.beer.service.web.model.inventory.BeerInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Slf4j
@Component
@Profile("!local-discovery")
public class BeerInventoryServiceRestTemplate implements IBeerInventoryService {

    public static final String INVENTORY_PATH = "/api/v1/beer/{beerId}/inventory";

    private final RestTemplate restTemplate;
    private final String inventoryUserName;
    private final String inventoryPassword;
    private final String beerInventoryServiceHost;


    @Autowired
    public BeerInventoryServiceRestTemplate(
            PropertiesConfiguration.SfgBreweryProperties sfgBreweryProperties,
            RestTemplateBuilder restTemplateBuilder) {

        this.beerInventoryServiceHost =
                sfgBreweryProperties.getBeerInventoryServiceHost();
        this.inventoryUserName = sfgBreweryProperties.getBeerInventoryUserName();
        this.inventoryPassword = sfgBreweryProperties.getBeerInventoryPassword();

        this.restTemplate = restTemplateBuilder
                .basicAuthentication(inventoryUserName, inventoryPassword)
                .build();
    }

    @Override
    public Integer getOnhandInventory(UUID beerId) {

        log.debug(">>>>>>> Calling Inventory Service");

        int sum = -1;

        try {
            ResponseEntity<List<BeerInventoryDto>> responseEntity =
                    restTemplate.exchange(
                            beerInventoryServiceHost + INVENTORY_PATH,
                            HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<BeerInventoryDto>>() {},
                            (Object) beerId);

            //sum from inventory list
            sum = Objects.requireNonNull(responseEntity.getBody())
                    .stream()
                    .mapToInt(BeerInventoryDto::getQuantityOnHand)
                    .sum();
        } catch (RestClientException e) {
            log.error(">>>>>>> Failed to retrieve inventory.");
            throw new NotFoundException("There is no inventory data available.");
        }

        return sum;
    }

//    public void setBeerInventoryServiceHost(String beerInventoryServiceHost) {
//        this.beerInventoryServiceHost = beerInventoryServiceHost;
//    }

}///:~