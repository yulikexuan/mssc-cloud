//: guru.sfg.beer.order.service.config.StateMachineConfigIT.java


package guru.sfg.beer.order.service.config;


import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("Test Order State Machine Configuration - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StateMachineConfigIT {

    @Autowired
    private StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum>
            stateMachineFactory;

    private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> orderStateMachine;

    @BeforeEach
    void setUp() {
        this.orderStateMachine = this.stateMachineFactory.getStateMachine(
                UUID.randomUUID());
    }

    @Test
    void test_State_Machine_Validation_Passed_Transitions() {

        // Given
        this.orderStateMachine.start();

        BeerOrderStatusEnum beerOrderInitialStatus =
                this.orderStateMachine.getState().getId();

        this.orderStateMachine.sendEvent(BeerOrderEventEnum.VALIDATE_ORDER_EVENT);
        this.orderStateMachine.sendEvent(BeerOrderEventEnum.VALIDATION_PASSED_EVENT);

        BeerOrderStatusEnum beerOrderCurrentStatus =
                this.orderStateMachine.getState().getId();

        // Then
        assertThat(beerOrderInitialStatus).isSameAs(BeerOrderStatusEnum.NEW);
        assertThat(beerOrderCurrentStatus).isSameAs(
                BeerOrderStatusEnum.VALIDATED);
    }

    @Test
    void test_State_Machine_Validation_Failed_Transitions() {

        // Given
        this.orderStateMachine.start();

        BeerOrderStatusEnum beerOrderInitialStatus =
                this.orderStateMachine.getState().getId();

        this.orderStateMachine.sendEvent(BeerOrderEventEnum.VALIDATE_ORDER_EVENT);
        this.orderStateMachine.sendEvent(BeerOrderEventEnum.VALIDATION_FAILED_EVENT);

        BeerOrderStatusEnum beerOrderValidationExceptionStatus =
                this.orderStateMachine.getState().getId();

        // Then
        assertThat(beerOrderInitialStatus).isSameAs(BeerOrderStatusEnum.NEW);
        assertThat(beerOrderValidationExceptionStatus)
                .isSameAs(BeerOrderStatusEnum.VALIDATION_EXCEPTION);
    }

}///:~