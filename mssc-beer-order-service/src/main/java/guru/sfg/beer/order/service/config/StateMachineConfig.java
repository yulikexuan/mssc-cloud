//: guru.sfg.beer.order.service.config.StateMachineConfig.java


package guru.sfg.beer.order.service.config;


import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;

import java.util.EnumSet;

import static guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.*;


@Slf4j
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends
        StateMachineConfigurerAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

    @Override
    public void configure(StateMachineStateConfigurer<
            BeerOrderStatusEnum, BeerOrderEventEnum> states) throws Exception {

        states.withStates()
                .initial(NEW)
                .states(EnumSet.allOf(BeerOrderStatusEnum.class))
                .end(CANCELLED)
                .end(DELIVERED)
                .end(PICKED_UP)
                .end(VALIDATION_EXCEPTION)
                .end(ALLOCATION_EXCEPTION)
                .end(DELIVERY_EXCEPTION);
    }

}///:~