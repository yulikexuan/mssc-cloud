//: guru.sfg.mssc.beer.service.domain.services.BeerOrderValidationRequestListener.java


package guru.sfg.mssc.beer.service.domain.services;


import guru.sfg.brewery.model.ValidateBeerOrderRequest;
import guru.sfg.mssc.beer.service.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Message;


@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderValidationRequestListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ORDER_VALIDATION_QUEUE_NAME)
    public void listenToBeerOrderValidationRequest(
            @Payload ValidateBeerOrderRequest validateBeerOrderRequest,
            @Headers MessageHeaders headers, Message message) throws JmsException {

        log.debug(">>>>>>> Received beer order validation request. ID - '{}'",
                validateBeerOrderRequest.getBeerOrderDto().getId());
    }

}///:~