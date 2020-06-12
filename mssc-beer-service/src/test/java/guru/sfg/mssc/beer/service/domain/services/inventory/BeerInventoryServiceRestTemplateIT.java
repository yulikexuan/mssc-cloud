//: guru.sfg.mssc.beer.service.domain.services.inventory.BeerInventoryServiceRestTemplateIT.java


package guru.sfg.mssc.beer.service.domain.services.inventory;


import guru.sfg.mssc.beer.service.bootstrap.BeerLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Disabled // It's just a manual test with a running beer inventory service
class BeerInventoryServiceRestTemplateIT {

    static final String BEER_1_UUID = "0a818933-087d-47f2-ad83-2f986ed087eb";

    @Autowired
    private BeerInventoryServiceRestTemplate inventoryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Given_Running_Inventory_Service_Then_Get_On_Hand_Inventory() {

        // Given
        UUID beerId = UUID.fromString(BEER_1_UUID);

        // When
        int qoh = this.inventoryService.getOnhandInventory(beerId);

        // Then
        assertThat(qoh).isGreaterThan(0);
    }

}///:~