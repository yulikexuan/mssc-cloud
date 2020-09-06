//: guru.sfg.mssc.beer.service.domain.services.BeerOrderValidationRequestListener.java


package guru.sfg.mssc.beer.service.domain.services;


import com.google.common.collect.ImmutableList;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import guru.sfg.brewery.model.ValidateBeerOrderRequest;
import guru.sfg.brewery.model.ValidateBeerOrderResponse;
import guru.sfg.mssc.beer.service.config.JmsConfig;
import guru.sfg.mssc.beer.service.domain.repositories.IBeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Message;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderValidationRequestListener {

    private final JmsTemplate jmsTemplate;

    private final IBeerRepository beerRepository;

    @Transactional
    @JmsListener(destination = JmsConfig.ORDER_VALIDATION_QUEUE_NAME)
    public void listenToBeerOrderValidationRequest(
            @Payload ValidateBeerOrderRequest validateBeerOrderRequest,
            @Headers MessageHeaders headers, Message message) throws JmsException {

        log.debug(">>>>>>> Received beer order validation request. ID - '{}'",
                validateBeerOrderRequest.getBeerOrderDto().getId());

        BeerOrderDto dto = validateBeerOrderRequest.getBeerOrderDto();

        List<String> unexistingUpcs = dto.getBeerOrderLines()
                .stream()
                .map(BeerOrderLineDto::getUpc)
                .filter(upc -> !beerRepository.existsBeerByUpc(upc))
                .collect(ImmutableList.toImmutableList());

        if (unexistingUpcs.size() > 0) {
            log.debug(">>>>>>> Beer Order validation failed." +
                    " Unexisting Order-UPC: {}", unexistingUpcs.toString());
        }

        ValidateBeerOrderResponse response = ValidateBeerOrderResponse.of(
                dto.getId(), unexistingUpcs);

        this.jmsTemplate.convertAndSend(
                JmsConfig.ORDER_VALIDATION_RESULT_QUEUE_NAME, response);
    }

}///:~