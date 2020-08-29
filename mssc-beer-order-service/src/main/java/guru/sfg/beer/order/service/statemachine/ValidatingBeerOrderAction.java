//: guru.sfg.beer.order.service.statemachine.ValidatingBeerOrderAction.java


package guru.sfg.beer.order.service.statemachine;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.beer.order.service.web.model.BeerOrderDto;
import guru.sfg.beer.order.service.web.model.ValidateBeerOrderRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static guru.sfg.beer.order.service.domain.BeerOrderEventEnum.VALIDATE_ORDER_EVENT;


@Slf4j
@Component
@AllArgsConstructor
public class ValidatingBeerOrderAction implements
        Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum>
            stateContext) {

        log.debug(">>>>>>> The {} event was called. ", VALIDATE_ORDER_EVENT);

        UUID beerOrderId = (UUID) stateContext.getMessageHeader(
                BeerOrderManager.BEER_ORDER_ID_HEADER);

        BeerOrder beerOrder = this.beerOrderRepository.findOneById(beerOrderId);
        BeerOrderDto beerOrderDto = this.beerOrderMapper.beerOrderToDto(beerOrder);

        // Send JMS Message to destination - 'validate-order'
        ValidateBeerOrderRequest request = ValidateBeerOrderRequest.of(beerOrderDto);
        this.jmsTemplate.convertAndSend(JmsConfig.ORDER_VALIDATION_QUEUE_NAME, request);

        log.debug(
                ">>>>>>> Sent validation request message to destination '{}' " +
                        "for beer order '{}'",
                JmsConfig.ORDER_VALIDATION_QUEUE_NAME,
                beerOrderId);
    }

}///:~