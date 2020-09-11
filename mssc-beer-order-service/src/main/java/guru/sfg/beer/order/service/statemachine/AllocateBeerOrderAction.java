//: guru.sfg.beer.order.service.statemachine.AllocateBeerOrderAction.java


package guru.sfg.beer.order.service.statemachine;


import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.brewery.model.AllocateBeerOrderRequest;
import guru.sfg.brewery.model.BeerOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static guru.sfg.beer.order.service.domain.BeerOrderEventEnum.ALLOCATE_ORDER_EVENT;


@Slf4j
@Component
@RequiredArgsConstructor
public class AllocateBeerOrderAction implements
        Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum>
                                    stateContext) {

        log.debug(">>>>>>> The {} event was called.", ALLOCATE_ORDER_EVENT);

        UUID beerOrderId = (UUID) stateContext.getMessageHeader(
                BeerOrderManager.BEER_ORDER_ID_HEADER);

        BeerOrder beerOrder = this.beerOrderRepository.findOneById(beerOrderId);
        BeerOrderDto beerOrderDto = this.beerOrderMapper.beerOrderToDto(beerOrder);

        // Send JMS Message to destination - "allocate-order"
        this.jmsTemplate.convertAndSend(
                JmsConfig.ORDER_ALLOCATION_QUEUE_NAME,
                AllocateBeerOrderRequest.of(beerOrderDto));

        log.debug(
                ">>>>>>> Sent allocation request message to destination '{}' " +
                        "for beer order '{}'",
                JmsConfig.ORDER_ALLOCATION_QUEUE_NAME, beerOrderId);
    }

}///:~