//: guru.sfg.beer.order.service.statemachine.BeerOrderValidationRequestListener.java


package guru.sfg.beer.order.service.statemachine;


import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.brewery.model.ValidateBeerOrderRequest;
import guru.sfg.brewery.model.ValidateBeerOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Slf4j
public class BeerOrderValidationRequestListener {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    @JmsListener(destination = JmsConfig.ORDER_VALIDATION_QUEUE_NAME)
    public void listenToBeerOrderValidationRequest(
            @Payload ValidateBeerOrderRequest validateBeerOrderRequest)
            throws JmsException {

        UUID orderId = validateBeerOrderRequest.getBeerOrderDto().getId();
        log.debug(">>>>>>> Received beer order validation request. ID - '{}'",
                orderId);

        ValidateBeerOrderResponse response = ValidateBeerOrderResponse.of(
                orderId, List.of());

        this.jmsTemplate.convertAndSend(
                JmsConfig.ORDER_VALIDATION_RESULT_QUEUE_NAME, response);
    }

}///:~