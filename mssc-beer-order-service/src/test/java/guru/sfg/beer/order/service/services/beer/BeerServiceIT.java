//: guru.sfg.beer.order.service.services.beer.BeerServiceIT.java


package guru.sfg.beer.order.service.services.beer;


import guru.sfg.beer.order.service.web.model.BeerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


/*
 * It's just a manual test with a running beer service
 */
//@Disabled
@SpringBootTest
class BeerServiceIT {

    static final String BEER_1_UUID = "0a818933-087d-47f2-ad83-2f986ed087eb";
    static final String UPC = "0631234200036";

    @Autowired
    private IBeerService beerService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Given_Beer_Id_When_Request_Beer_Then_Get_BeerDto_Back() {

        // Given
        UUID beerId = UUID.fromString(BEER_1_UUID);

        // When & Then
        await().untilAsserted(() -> this.beerService.getBeerById(beerId)
                .thenAccept(beerDtoOpt -> {
                    BeerDto beerDto = beerDtoOpt.get();
                    assertThat(beerDto).isNotNull();
                    assertThat(beerDto.getBeerName()).isEqualTo("Mango Bobs");
                    assertThat(beerDto.getBeerStyle()).isEqualTo("IPA");
                }));
    }

    @Test
    void test_Given_UPC_When_Request_Beer_Then_Get_BeerDto_Back() {

        // Given
        UUID beerId = UUID.fromString(BEER_1_UUID);

        // When & Then
        await().untilAsserted(() -> this.beerService.getBeerByUpc(UPC)
                .thenAccept(beerDtoOpt -> {
                    BeerDto beerDto = beerDtoOpt.get();
                    assertThat(beerDto).isNotNull();
                    assertThat(beerDto.getBeerName()).isEqualTo("Mango Bobs");
                    assertThat(beerDto.getBeerStyle()).isEqualTo("IPA");
                }));
    }

}///:~