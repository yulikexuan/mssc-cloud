//: guru.sfg.beer.order.service.statemachine.DeallocateBeerOrderAction.java


package guru.sfg.beer.order.service.statemachine;


import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@Component
public class DeallocateBeerOrderAction implements
        Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    @Override
    public void execute(
            StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {

        UUID beerOrderId = (UUID) context.getMessageHeader(
                BeerOrderManager.BEER_ORDER_ID_HEADER);

        log.debug(">>>>>>> Deallocating Beer Order {}", beerOrderId);
    }

}///:~