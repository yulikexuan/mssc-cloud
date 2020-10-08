//: guru.sfg.beer.order.service.statemachine.listener.BeerOrderAllocationListener.java


package guru.sfg.beer.order.service.statemachine.listener;


import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.CustomerReferences;
import guru.sfg.brewery.model.AllocateBeerOrderRequest;
import guru.sfg.brewery.model.AllocateBeerOrderResponse;
import guru.sfg.brewery.model.BeerOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderAllocationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ORDER_ALLOCATION_QUEUE_NAME)
    public void listenToBeerOrderAllocationEvent(
            @Payload AllocateBeerOrderRequest allocateBeerOrderRequest,
            Message message) {

        BeerOrderDto beerOrderDto = Objects.requireNonNull(
                allocateBeerOrderRequest.getBeerOrderDto());

        log.debug(">>>>>>> [IT] Processing beer order allocation {} ... ...",
                beerOrderDto.getId());

        boolean hasAllocationError =
                CustomerReferences.CUSTOMER_REF_FAILED_ALLOCATION
                        .equals(beerOrderDto.getCustomerRef());

        boolean isPendingInventory = false;

        if (!hasAllocationError) {
            isPendingInventory =
                CustomerReferences.CUSTOMER_REF_PENDING_INVENTORY
                        .equals(beerOrderDto.getCustomerRef());
        }

        AllocateBeerOrderResponse.AllocateBeerOrderResponseBuilder builder =
                AllocateBeerOrderResponse.builder();

        beerOrderDto.getBeerOrderLines().stream()
                .forEach(line -> {
                    line.setQuantityAllocated(line.getOrderQuantity());
                });

        builder.beerOrderDto(beerOrderDto);
        builder.pendingInventory(isPendingInventory);
        builder.allocationError(hasAllocationError);

        this.jmsTemplate.convertAndSend(
                JmsConfig.ORDER_ALLOCATION_RESPONSE_QUEUE_NAME,
                builder.build());
    }

}///:~