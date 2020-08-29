//: guru.sfg.beer.order.service.statemachine.BeerOrderManagerIT.java


package guru.sfg.beer.order.service.statemachine;


import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@DisplayName("Beer Order State Machine Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BeerOrderManagerIT {

    private Customer customer;
    private BeerOrder beerOrder;

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private BeerOrderManager beerOrderManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Given_New_State_Beer_Ordere_When_Sending_Validate_Order_Event_Then_In_Validation_Pending_State() {

        // Given
        this.beerOrder = BeerOrder.builder().build();

        // When
        BeerOrder beerOrder = this.beerOrderManager.newBeerOrder(this.beerOrder);

        // Then
        assertThat(beerOrder.getOrderStatus()).isSameAs(
                BeerOrderStatusEnum.VALIDATION_PENDING);
    }

}///:~