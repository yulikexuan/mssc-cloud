//: guru.sfg.beer.order.service.statemachine.AllocationFailureAction.java


package guru.sfg.beer.order.service.statemachine;


import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.brewery.model.AllocationFailureTransactionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class AllocationFailureTransactionRequestAction implements
        Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(
            StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {

        UUID beerOrderId = (UUID) context.getMessageHeader(
                BeerOrderManager.BEER_ORDER_ID_HEADER);

        log.error(">>>>>>> [Allocation Failure]: Sending Request Allocation " +
                "Failure Transaction {}", beerOrderId);

        AllocationFailureTransactionRequest request =
                AllocationFailureTransactionRequest.of(beerOrderId);

        this.jmsTemplate.convertAndSend(
                JmsConfig.ORDER_ALLOCATION_FAILURE_TX_REQUEST_QUEUE_NAME,
                request);
    }

}///:~