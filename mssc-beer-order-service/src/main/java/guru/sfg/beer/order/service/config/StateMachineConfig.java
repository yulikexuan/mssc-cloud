//: guru.sfg.beer.order.service.config.StateMachineConfig.java


package guru.sfg.beer.order.service.config;


import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

import static guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.*;
import static guru.sfg.beer.order.service.domain.BeerOrderEventEnum.*;


@Slf4j
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends
        StateMachineConfigurerAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

    @Override
    public void configure(
            StateMachineStateConfigurer<BeerOrderStatusEnum, BeerOrderEventEnum>
                    states) throws Exception {

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

    /*
     * A transition is a relationship between a source state and a target state
     * A switch from a state to another is a state transition caused by a trigger
     *
     * Internal Transition
     *   - Internal transition is used when action needs to be executed without
     *     causing a state transition
     *   - With internal transition source and target state is always a same and
     *     it is identical with self-transition in the absence of state entry
     *     and exit actions
     *
     * External vs. Local Transition
     *   - Most of the cases external and local transition are functionally
     *     equivalent except in cases where transition is happening between
     *     super and sub states
     *   - Local transition doesn’t cause exit and entry to source state if
     *     target state is a substate of a source state. Other way around,
     *     local transition doesn’t cause exit and entry to target state if
     *     target is a superstate of a source state.
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<
            BeerOrderStatusEnum, BeerOrderEventEnum> transitions) throws Exception {

        transitions.withExternal()
                .source(NEW)
                .target(NEW)
                .event(VALIDATE_ORDER_EVENT)
                // TODO: Add validation action here
                .and()
                .withExternal()
                .source(NEW)
                .target(VALIDATED)
                .event(VALIDATION_PASSED_EVENT)
                .and()
                .withExternal()
                .source(NEW)
                .target(VALIDATION_EXCEPTION)
                .event(VALIDATION_FAILED_EVENT);
    }

}///:~