//: guru.sfg.mssc.beer.service.domain.services.inventory.BeerInventoryServiceRestTemplate.java


package guru.sfg.mssc.beer.service.domain.services.inventory;


import guru.sfg.mssc.beer.service.config.PropertiesConfiguration;
import guru.sfg.mssc.beer.service.web.model.inventory.BeerInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
public class BeerInventoryServiceRestTemplate implements IBeerInventoryService {

    public static final String INVENTORY_PATH = "/api/v1/beer/{beerId}/inventory";

    private final RestTemplate restTemplate;

    private String beerInventoryServiceHost;

    @Autowired
    public BeerInventoryServiceRestTemplate(
            PropertiesConfiguration.SfgBreweryProperties sfgBreweryProperties,
            RestTemplateBuilder restTemplateBuilder) {

        this.restTemplate = restTemplateBuilder.build();
        this.beerInventoryServiceHost =
                sfgBreweryProperties.getBeerInventoryServiceHost();
    }

    @Override
    public Integer getOnhandInventory(UUID beerId) {

        log.debug("Calling Inventory Service");

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
        }

        return sum;
    }

    public void setBeerInventoryServiceHost(String beerInventoryServiceHost) {
        this.beerInventoryServiceHost = beerInventoryServiceHost;
    }

}///:~