//: guru.sfg.beer.order.service.services.beer.BeerServiceIT.java


package guru.sfg.beer.order.service.services.beer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.ConfigureWireMock;
import com.github.jenspiegsa.wiremockextension.InjectServer;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.jenspiegsa.wiremockextension.WireMockSettings;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.Options;
import guru.sfg.beer.order.service.web.model.BeerDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


/*
 * It's just a manual test with a running beer service
 */
@Slf4j
@SpringBootTest
@ExtendWith(WireMockExtension.class)
@WireMockSettings(failOnUnmatchedRequests = true)
@DisplayName("Beer Service Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BeerServiceIT {

    static final String BEER_1_UUID = "0a818933-087d-47f2-ad83-2f986ed087eb";
    static final String UPC = "0631234200036";
    static final String BEER_NAME = "Mango Bobs";
    static final String BEER_STYLE = "IPA";

//    @Autowired
//    private PropertiesConfiguration.SfgBreweryProperties sfgBreweryProperties;

    @InjectServer
    private WireMockServer wireMockServer;

    @ConfigureWireMock
    Options options = wireMockConfig()
            .port(8099)
            .notifier(new ConsoleNotifier(true));

// Using @TestConfiguration to release Beans
//
//    @TestConfiguration
//    static class RestTemplateBuilderProvider {
//
//        @Bean(destroyMethod = "stop")
//        WireMockServer wireMockServer() {
//            WireMockServer wireMockServer = with(wireMockConfig().port(8081));
//            wireMockServer.start();
//            return wireMockServer;
//        }
//    }
//    @Autowired
//    private WireMockServer wireMockServer;

    @Autowired
    private IBeerService beerService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Given_Beer_Id_When_Request_Beer_Then_Get_BeerDto_Back()
            throws JsonProcessingException {

        // Given
        BeerDto beerDto = beerDto();
        String expectResponseBody = this.objectMapper.writeValueAsString(beerDto);
        String uri = getBeerDtoUri(BeerService.BEER_PATH_V1, BEER_1_UUID);

        this.wireMockServer.stubFor(get(uri).willReturn(okJson(expectResponseBody)));

        // When
        CompletableFuture<Optional<BeerDto>> completableFuture =
                this.beerService.getBeerById(UUID.fromString(BEER_1_UUID));

        // Then
        assertResponse(completableFuture);
    }

    @Test
    void test_Given_Beer_UPC_When_Request_Beer_Then_Get_BeerDto_Back()
            throws JsonProcessingException {

        // Given
        BeerDto beerDto = beerDto();
        String expectResponseBody = this.objectMapper.writeValueAsString(beerDto);
        String uri = getBeerDtoUri(BeerService.BEER_UPC_PATH_V1, UPC);

        this.wireMockServer.stubFor(get(uri).willReturn(okJson(expectResponseBody)));

        // When
        CompletableFuture<Optional<BeerDto>> completableFuture =
                this.beerService.getBeerByUpc(UPC);

        // Then
        assertResponse(completableFuture);
    }

    private void assertResponse(
            CompletableFuture<Optional<BeerDto>> completableFuture) {
        await().untilAsserted(() -> {
            Optional<BeerDto> beerDtoOpt = completableFuture.get();
            assertThat(beerDtoOpt.isPresent()).isTrue();
            BeerDto actualBeerDto = beerDtoOpt.get();
            assertThat(actualBeerDto.getBeerName()).isEqualTo(BEER_NAME);
            assertThat(actualBeerDto.getBeerStyle()).isEqualTo(BEER_STYLE);
        });
    }

    private BeerDto beerDto() {
        return BeerDto.builder()
                .id(UUID.fromString(BEER_1_UUID))
                .upc(UPC)
                .beerName(BEER_NAME)
                .beerStyle(BEER_STYLE)
                .build();
    }

    private String getBeerDtoUri(@NonNull String uri, @NonNull String path) {
        return String.join("", uri, path);
    }

}///:~