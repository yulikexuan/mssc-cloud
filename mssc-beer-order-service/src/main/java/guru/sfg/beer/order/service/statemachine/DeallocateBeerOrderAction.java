//: guru.sfg.beer.order.service.statemachine.DeallocateBeerOrderAction.java


package guru.sfg.beer.order.service.statemachine;


import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.DeallocateBeerOrderRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class DeallocateBeerOrderAction implements
        Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    @Transactional
    public void execute(
            StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {

        UUID beerOrderId = (UUID) context.getMessageHeader(
                BeerOrderManager.BEER_ORDER_ID_HEADER);

        log.debug(">>>>>>> Deallocating Beer Order {}", beerOrderId);

        this.beerOrderRepository.findById(beerOrderId)
                .ifPresentOrElse(
                        this::sendDeallocationMessage,
                        () -> log.error(String.format(
                                ">>>>>>> Beer order not found for deallocation: %s",
                                beerOrderId.toString()))
                );
    }

    private void sendDeallocationMessage(@NonNull BeerOrder beerOrder) {
        BeerOrderDto beerOrderDto = this.beerOrderMapper.beerOrderToDto(
                beerOrder);
        DeallocateBeerOrderRequest request = DeallocateBeerOrderRequest.of(
                beerOrderDto);
        log.debug(">>>>>>> Sending order deallocation request {}",
                beerOrderDto.getId());
        this.jmsTemplate.convertAndSend(JmsConfig.ORDER_DEALLOCATION_QUEUE_NAME,
                request);
    }

}///:~