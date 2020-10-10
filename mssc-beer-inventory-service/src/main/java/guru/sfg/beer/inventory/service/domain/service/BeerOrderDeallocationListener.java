//: guru.sfg.beer.inventory.service.domain.service.BeerOrderDeallocationListener.java


package guru.sfg.beer.inventory.service.domain.service;


import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.brewery.model.DeallocateBeerOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderDeallocationListener {

    private final IBeerAllocationService beerAllocationService;

    @JmsListener(destination = JmsConfig.ORDER_DEALLOCATION_QUEUE_NAME)
    void listenToBeerOrderDeallocationMessage(
            @Payload DeallocateBeerOrderRequest deallocateBeerOrderRequest,
            Message message) {

        this.beerAllocationService.deallocateBeerOrder(
                deallocateBeerOrderRequest.getBeerOrderDto());
    }

}///:~