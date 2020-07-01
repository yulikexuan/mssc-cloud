package guru.sfg.beer.inventory.service.bootstrap;

import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by jt on 2019-06-07.
 */
@Slf4j
@RequiredArgsConstructor
// @Component
public class BeerInvntoryBootstrap implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";
    public static final String BEER_4_UPC = "0072890000224";
    public static final String BEER_5_UPC = "0071990316006";
    public static final String BEER_6_UPC = "0640265808593";
    public static final String BEER_7_UPC = "0640265808609";

    public static final UUID BEER_1_UUID = UUID.fromString("0a818933-087d-47f2-ad83-2f986ed087eb");
    public static final UUID BEER_2_UUID = UUID.fromString("a712d914-61ea-4623-8bd0-32c0f6545bfd");
    public static final UUID BEER_3_UUID = UUID.fromString("026cc3c8-3a0c-4083-a05b-e908048c1b08");
    public static final UUID BEER_4_UUID = UUID.fromString("b36541b8-ad9e-11ea-a64d-0242ac130004");
    public static final UUID BEER_5_UUID = UUID.fromString("faeafc03-73ee-4652-b55c-1e2beedb457f");
    public static final UUID BEER_6_UUID = UUID.fromString("1a294bd8-ad9f-11ea-a64d-0242ac130004");
    public static final UUID BEER_7_UUID = UUID.fromString("c8a865d2-a647-4c20-a7ed-67eb0cbfb054");

    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if(beerInventoryRepository.count() == 0){
            loadInitialInv();
        }
    }

    private void loadInitialInv() {
        beerInventoryRepository.save(BeerInventory
                .builder()
                .beerId(BEER_1_UUID)
                .upc(BEER_1_UPC)
                .quantityOnHand(30)
                .build());

        beerInventoryRepository.save(BeerInventory
                .builder()
                .beerId(BEER_2_UUID)
                .upc(BEER_2_UPC)
                .quantityOnHand(40)
                .build());

        beerInventoryRepository.saveAndFlush(BeerInventory
                .builder()
                .beerId(BEER_3_UUID)
                .upc(BEER_3_UPC)
                .quantityOnHand(50)
                .build());

        beerInventoryRepository.saveAndFlush(BeerInventory
                .builder()
                .beerId(BEER_4_UUID)
                .upc(BEER_4_UPC)
                .quantityOnHand(40)
                .build());

        beerInventoryRepository.saveAndFlush(BeerInventory
                .builder()
                .beerId(BEER_5_UUID)
                .upc(BEER_5_UPC)
                .quantityOnHand(30)
                .build());

        beerInventoryRepository.saveAndFlush(BeerInventory
                .builder()
                .beerId(BEER_6_UUID)
                .upc(BEER_6_UPC)
                .quantityOnHand(20)
                .build());

        beerInventoryRepository.saveAndFlush(BeerInventory
                .builder()
                .beerId(BEER_7_UUID)
                .upc(BEER_7_UPC)
                .quantityOnHand(55)
                .build());

        log.debug("Loaded Inventory. Record count: " + beerInventoryRepository.count());
    }
}
