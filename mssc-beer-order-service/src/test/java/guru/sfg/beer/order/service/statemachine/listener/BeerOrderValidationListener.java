//: guru.sfg.beer.order.service.statemachine.listener.BeerOrderValidationListener.java


package guru.sfg.beer.order.service.statemachine.listener;


import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.brewery.model.ValidateBeerOrderRequest;
import guru.sfg.brewery.model.ValidateBeerOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderValidationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ORDER_VALIDATION_QUEUE_NAME)
    public void listenToBeerOrderValidationMessage(
            @Payload ValidateBeerOrderRequest validateBeerOrderRequest,
            Message message) {

        UUID beerOrderId = validateBeerOrderRequest.getBeerOrderDto().getId();

        log.debug(">>>>>>> Validating Beer Order: {}", beerOrderId);

        ValidateBeerOrderResponse response = ValidateBeerOrderResponse.of(
                beerOrderId, List.of());

        this.jmsTemplate.convertAndSend(
                JmsConfig.ORDER_VALIDATION_RESULT_QUEUE_NAME, response);
    }

}///:~