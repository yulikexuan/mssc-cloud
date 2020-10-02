//: guru.sfg.beer.order.service.statemachine.BeerOrderManagerIT.java


package guru.sfg.beer.order.service.statemachine;


import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@DisplayName("Beer Order Manager Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BeerOrderManagerIT {

    private static final String BEER_ID_HEINEKEN =
            "b36541b8-ad9e-11ea-a64d-0242ac130004";

    private static final String BEER_ID_GALAXY_CAT =
            "a712d914-61ea-4623-8bd0-32c0f6545bfd";

    private static final String UPC_HEINEKEN = "0072890000224";

    private static final String UPC_GALAXY_CAT = "0631234300019";

    private BeerOrder beerOrder;

    @Autowired
    private BeerOrderManager beerOrderManager;

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        this.customer = this.customerRepository.save(Customer.builder()
                .customerName("tester")
                .build());
        this.beerOrder = this.createBeerOrder();
    }

    @Test
    @Transactional
    void test_Given_New_Beer_Ordere_Then_Allocate() {

        // Given

        // When
        BeerOrder beerOrder = this.beerOrderManager.newBeerOrder(this.beerOrder);

        // Then
        assertThat(beerOrder).isNotNull();
        assertThat(beerOrder.getOrderStatus()).isSameAs(
                BeerOrderStatusEnum.VALIDATED);
    }

    private BeerOrder createBeerOrder() {

        BeerOrderLine line_1 = BeerOrderLine.builder()
                .upc(UPC_HEINEKEN)
                .orderQuantity(50)
                .build();

        BeerOrderLine line_2 = BeerOrderLine.builder()
                .upc(UPC_GALAXY_CAT)
                .orderQuantity(40)
                .build();

        Set<BeerOrderLine> lines = Sets.newHashSet();
        lines.add(line_1);
        lines.add(line_2);

        this.beerOrder = BeerOrder.builder()
                .customer(this.customer)
                .beerOrderLines(lines)
                .build();

        line_1.setBeerOrder(this.beerOrder);
        line_2.setBeerOrder(this.beerOrder);

        return this.beerOrder;
    }

}///:~