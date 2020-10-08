//: guru.sfg.beer.order.service.statemachine.BeerOrderManagerIT.java


package guru.sfg.beer.order.service.statemachine;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.ConfigureWireMock;
import com.github.jenspiegsa.wiremockextension.InjectServer;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.jenspiegsa.wiremockextension.WireMockSettings;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.Options;
import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.services.beer.BeerService;
import guru.sfg.beer.order.service.web.model.BeerDto;
import guru.sfg.brewery.model.AllocationFailureTransactionRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.*;
import static guru.sfg.beer.order.service.statemachine.listener.CustomerReferences.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@Slf4j
@SpringBootTest
@ExtendWith(WireMockExtension.class)
@WireMockSettings(failOnUnmatchedRequests = true)
@DisplayName("Beer Order Manager Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BeerOrderManagerIT {

    private static final String BEER_ID_HEINEKEN =
            "b36541b8-ad9e-11ea-a64d-0242ac130004";
    private static final String BEER_ID_GALAXY_CAT =
            "a712d914-61ea-4623-8bd0-32c0f6545bfd";

    private static final String UPC_HEINEKEN = "0072890000224";
    private static final String UPC_GALAXY_CAT = "0631234300019";

    public static final String BEER_NAME_HEINEKEN = "Heineken";
    public static final String BEER_STYLE_HEINEKEN = "PALE_LARGE";
    public static final double BEER_PRICE_HEINEKEN = 12.99;
    public static final String BEER_NAME_GALAXY_CAT = "Galaxy Cat";
    public static final String BEER_STYPE_GALAXY_CAT = "PALE_ALE";
    public static final double BEER_PRICE_GALAXY_CAT = 13.95;

    @InjectServer
    private WireMockServer wireMockServer;

    @ConfigureWireMock
    Options options = wireMockConfig()
            .port(8099)
            .notifier(new ConsoleNotifier(true));

    private BeerOrder beerOrder;

    @Autowired
    private BeerOrderManager beerOrderManager;

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer customer;

    private BeerDto orderLineHeineken;
    private String orderLineHeinekenJson;
    private String orderLineHeinekenUri;

    private BeerDto orderLineGalaxyCat;
    private String orderLineGalaxyCatJson;
    private String orderLineGalaxyCatUri;

    @BeforeEach
    void setUp() throws Exception {

        this.customer = customerRepository.save(Customer.builder()
                .customerName("tester")
                .build());

        this.orderLineHeineken = getBeerDtoByUpcHeineken();
        this.orderLineHeinekenJson =
                this.objectMapper.writeValueAsString(this.orderLineHeineken);
        this.orderLineHeinekenUri = this.getBeerDtoUri(
                BeerService.BEER_UPC_PATH_V1, UPC_HEINEKEN);

        this.orderLineGalaxyCat = getBeerDtoByUpcGalaxyCat();
        this.orderLineGalaxyCatJson =
                this.objectMapper.writeValueAsString(this.orderLineGalaxyCat);
        this.orderLineGalaxyCatUri = this.getBeerDtoUri(
                BeerService.BEER_UPC_PATH_V1, UPC_GALAXY_CAT);
    }

    @Nested
    @DisplayName("Test the happy path of BeerOrderManager - ")
    class HappyPathTest {

        private static final String CUSTOMER_REF = CUSTOMER_REF_HAPPY_PATH;

        @BeforeEach
        void setUp() throws Exception {
            beerOrder = createBeerOrder(customer, CUSTOMER_REF);
        }

        @Test
        void test_Given_New_Beer_Order_Then_Ending_As_Allocated_State()
                throws Exception {

            // Given
            preparingWireMockStubsForOrderLines();

            // When
            UUID beerOrderId = beerOrderManager
                    .newBeerOrder(beerOrder).orElse(null);

            // Then
            await().untilAsserted(() -> {
                assertThat(beerOrderRepository.findById(beerOrderId).
                        isPresent()).isTrue();
                BeerOrder allocatedBeerOrder = beerOrderRepository
                        .findById(beerOrderId).get();
                assertThat(allocatedBeerOrder.getOrderStatus()).isEqualTo(ALLOCATED);
                allocatedBeerOrder.getBeerOrderLines().forEach(
                        line -> assertThat(line.getOrderQuantity()).isEqualTo(
                                line.getQuantityAllocated()));
            });
        }

        @Test
        void test_Given_New_Beer_Order_Then_Ending_As_PickedUp_State()
                throws Exception {

            // Given
            preparingWireMockStubsForOrderLines();
            UUID beerOrderId = beerOrderManager.newBeerOrder(beerOrder)
                    .orElseThrow(IllegalStateException::new);

            await().untilAsserted(() -> {
                assertThat(beerOrderRepository.findById(beerOrderId).
                        isPresent()).isTrue();
                BeerOrder allocatedBeerOrder = beerOrderRepository
                        .findById(beerOrderId).get();
                assertThat(allocatedBeerOrder.getOrderStatus()).isEqualTo(ALLOCATED);
                allocatedBeerOrder.getBeerOrderLines().forEach(
                        line -> assertThat(line.getOrderQuantity()).isEqualTo(
                                line.getQuantityAllocated()));
            });

            // When
            beerOrderManager.beerOrderPickedUp(beerOrderId);

            // Then
            await().untilAsserted(() -> {
                BeerOrder pickedUpOrder = beerOrderRepository
                        .findById(beerOrderId).get();
                assertThat(pickedUpOrder.getOrderStatus()).isEqualTo(PICKED_UP);
            });
        }

    }//: End of HappyPathTest

    @Nested
    @DisplayName("Test Failed Saga Calls - ")
    class SagaFailedTest {

        @BeforeEach
        void setUp() throws Exception {
        }

        @Test
        void test_Given_New_Beer_Order_Then_Ending_As_VALIDATION_EXCEPTION_State()
                throws Exception {

            // Given
            preparingWireMockStubsForOrderLines();

            beerOrder = createBeerOrder(customer, CUSTOMER_REF_FAILED_VALIDATION);

            UUID beerOrderId = beerOrderManager.newBeerOrder(beerOrder)
                    .orElseThrow(IllegalStateException::new);

            // Then
            await().untilAsserted(() -> {
                BeerOrder invalidOrder = beerOrderRepository
                        .findById(beerOrderId).get();
                assertThat(invalidOrder.getOrderStatus()).isEqualTo(
                        VALIDATION_EXCEPTION);
            });
        }

        @Test
        void test_Given_New_Beer_Order_Then_Ending_As_ALLOCATION_EXCEPTION_State()
            throws Exception {

            // Given
            preparingWireMockStubsForOrderLines();

            beerOrder = createBeerOrder(customer, CUSTOMER_REF_FAILED_ALLOCATION);

            // When
            UUID beerOrderId = beerOrderManager
                    .newBeerOrder(beerOrder).orElse(null);

            // Then
            await().untilAsserted(() -> {
                assertThat(beerOrderRepository.findById(beerOrderId).
                        isPresent()).isTrue();
                BeerOrder allocatedBeerOrder = beerOrderRepository
                        .findById(beerOrderId).get();
                assertThat(allocatedBeerOrder.getOrderStatus()).isEqualTo(
                        ALLOCATION_EXCEPTION);
                String destination = JmsConfig
                        .ORDER_ALLOCATION_FAILURE_TX_REQUEST_QUEUE_NAME;
                AllocationFailureTransactionRequest txRequest =
                        (AllocationFailureTransactionRequest) jmsTemplate
                                .receiveAndConvert(destination);
                UUID failedBeerOrderId = txRequest.getBeerOrderId();
                assertThat(failedBeerOrderId).isEqualTo(beerOrderId);
            });
        }

        @Test
        void test_Given_New_Beer_Order_Then_Ending_As_PENDING_INVENTORY_State()
                throws Exception {

            // Given
            preparingWireMockStubsForOrderLines();

            beerOrder = createBeerOrder(customer, CUSTOMER_REF_PENDING_INVENTORY,
                    true);

            // When
            UUID beerOrderId = beerOrderManager
                    .newBeerOrder(beerOrder).orElse(null);

            // Then
            await().untilAsserted(() -> {
                assertThat(beerOrderRepository.findById(beerOrderId).
                        isPresent()).isTrue();
                BeerOrder allocatedBeerOrder = beerOrderRepository
                        .findById(beerOrderId).get();
                assertThat(allocatedBeerOrder.getOrderStatus()).isEqualTo(
                        PENDING_INVENTORY);
            });
        }

    }

    private void preparingWireMockStubsForOrderLines() {
        this.wireMockServer.stubFor(get(this.orderLineHeinekenUri).willReturn(
                okJson(this.orderLineHeinekenJson)));
        this.wireMockServer.stubFor(get(this.orderLineGalaxyCatUri).willReturn(
                okJson(this.orderLineGalaxyCatJson)));
    }

    private BeerOrder createBeerOrder(@NonNull final Customer customer,
                                      @NonNull final String customerRef) {

        return createBeerOrder(customer, customerRef, false);
    }

    private BeerOrder createBeerOrder(
            @NonNull final Customer customer,
            @NonNull final String customerRef,
            boolean pendingInventory) {

        BeerOrderLine line_1 = BeerOrderLine.builder()
                .upc(UPC_HEINEKEN)
                .orderQuantity(50)
                .quantityAllocated(50)
                .build();

        BeerOrderLine line_2 = BeerOrderLine.builder()
                .upc(UPC_GALAXY_CAT)
                .orderQuantity(40)
                .quantityAllocated(pendingInventory ? 20 : 40)
                .build();

        Set<BeerOrderLine> lines = Sets.newHashSet();
        lines.add(line_1);
        lines.add(line_2);

        this.beerOrder = BeerOrder.builder()
                .customer(customer)
                .customerRef(customerRef)
                .beerOrderLines(lines)
                .build();

        line_1.setBeerOrder(this.beerOrder);
        line_2.setBeerOrder(this.beerOrder);

        return this.beerOrder;
    }

    private BeerDto getBeerDtoByUpcHeineken() {
        return BeerDto.builder()
                .id(UUID.fromString(BEER_ID_HEINEKEN))
                .upc(UPC_HEINEKEN)
                .beerName(BEER_NAME_HEINEKEN)
                .beerStyle(BEER_STYLE_HEINEKEN)
                .price(BigDecimal.valueOf(BEER_PRICE_HEINEKEN))
                .build();
    }

    private BeerDto getBeerDtoByUpcGalaxyCat() {
        return BeerDto.builder()
                .id(UUID.fromString(BEER_ID_GALAXY_CAT))
                .upc(UPC_GALAXY_CAT)
                .beerName(BEER_NAME_GALAXY_CAT)
                .beerStyle(BEER_STYPE_GALAXY_CAT)
                .price(BigDecimal.valueOf(BEER_PRICE_GALAXY_CAT))
                .build();
    }

    private String getBeerDtoUri(@NonNull String uri, @NonNull String path) {
        return String.join("", uri, path);
    }

}///:~