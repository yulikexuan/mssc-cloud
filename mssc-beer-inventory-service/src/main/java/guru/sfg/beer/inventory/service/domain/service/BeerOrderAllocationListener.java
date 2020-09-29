//: guru.sfg.beer.inventory.service.domain.service.BeerOrderAllocationListener.java


package guru.sfg.beer.inventory.service.domain.service;


import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.brewery.model.AllocateBeerOrderRequest;
import guru.sfg.brewery.model.AllocateBeerOrderResponse;
import guru.sfg.brewery.model.BeerOrderDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderAllocationListener {

    private final JmsTemplate jmsTemplate;
    private final IBeerAllocationService beerAllocationService;

    @JmsListener(destination = JmsConfig.ORDER_ALLOCATION_QUEUE_NAME)
    public void listenToBeerOrderAllocationEvent(
            @NonNull AllocateBeerOrderRequest allocateBeerOrderRequest) {

        AllocateBeerOrderResponse.AllocateBeerOrderResponseBuilder builder =
                AllocateBeerOrderResponse.builder();

        boolean allocationError = false;

        try {

            log.debug(">>>>>>> Processing beer order allocation.");

            BeerOrderDto beerOrderDto = Objects.requireNonNull(
                    allocateBeerOrderRequest.getBeerOrderDto());

            builder.beerOrderDto(beerOrderDto);

            boolean allocated = this.beerAllocationService.allocateBeerOrder(
                    beerOrderDto);
            boolean pendingInventory = !allocated;

            builder.pendingInventory(pendingInventory);

        } catch (Exception e) {
            allocationError = true;
            log.error(">>>>>>> The beer order allocation process was failed by {}",
                    e.getCause());
        }

        builder.allocationError(allocationError);

        this.jmsTemplate.convertAndSend(
                JmsConfig.ORDER_ALLOCATION_RESPONSE_QUEUE_NAME,
                builder.build());
    }

}///:~