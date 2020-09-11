//: guru.sfg.beer.order.service.statemachine.BeerOrderManager.java


package guru.sfg.beer.order.service.statemachine;


import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.brewery.model.ValidateBeerOrderResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Message;
import java.util.UUID;

import static guru.sfg.beer.order.service.domain.BeerOrderEventEnum.*;


@Slf4j
@Service
@AllArgsConstructor
public class BeerOrderManager implements IBeerOrderManager {

    public static final String BEER_ORDER_ID_HEADER = "beer_order_id";

    private final BeerOrderRepository beerOrderRepository;

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum>
            stateMachineFactory;

    private final StateMachineInterceptor<BeerOrderStatusEnum, BeerOrderEventEnum>
            stateMachineInterceptor;

    @Override
    @Transactional
    public BeerOrder newBeerOrder(@NonNull BeerOrder beerOrder) {

        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);

        BeerOrder newBeerOrder = this.beerOrderRepository.save(beerOrder);

        this.sendBeerOrderEvent(newBeerOrder, VALIDATE_ORDER_EVENT);

        return newBeerOrder;
    }

    @Transactional
    @JmsListener(destination = JmsConfig.ORDER_VALIDATION_RESULT_QUEUE_NAME)
    void listenToBeerOrderValidationResult(
            @Payload ValidateBeerOrderResponse validationResponse,
            @Headers MessageHeaders headers, Message message) {

        UUID beerOrderId = validationResponse.getOrderId();
        boolean isOrderValide = validationResponse.isValid();

        log.debug(">>>>>>> Is Beer-Order '{}' valide? {}", beerOrderId,
                isOrderValide);

        BeerOrder beerOrder = this.beerOrderRepository.findOneById(
                beerOrderId);

        if (isOrderValide) {
            this.sendBeerOrderEvent(beerOrder, VALIDATION_PASSED_EVENT);
            BeerOrder validatedBeerOrder = this.beerOrderRepository.findOneById(
                    beerOrderId);
            this.sendBeerOrderEvent(validatedBeerOrder, ALLOCATE_ORDER_EVENT);
        } else {
            this.sendBeerOrderEvent(beerOrder, VALIDATION_FAILED_EVENT);
        }
    }

    private void sendBeerOrderEvent(@NonNull BeerOrder beerOrder,
                                    @NonNull BeerOrderEventEnum event) {

        var stateMachine =
                this.buildStateMachine(beerOrder);

        var msg = MessageBuilder.withPayload(event)
                .setHeader(BEER_ORDER_ID_HEADER, beerOrder.getId())
                .build();

        stateMachine.sendEvent(msg);
    }

    private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum>
    buildStateMachine(BeerOrder beerOrder) {

        var stateMachine =
                this.stateMachineFactory.getStateMachine(beerOrder.getId());

        stateMachine.stop();

        StateMachineContext<BeerOrderStatusEnum, BeerOrderEventEnum>
                stateMachineContext = new DefaultStateMachineContext<>(
                        beerOrder.getOrderStatus(), null, null,
                null);

        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(stateMachineAccessor -> {
                        stateMachineAccessor.addStateMachineInterceptor(
                                this.stateMachineInterceptor);
                        stateMachineAccessor.resetStateMachine(
                                stateMachineContext);
                });

        stateMachine.start();

        return stateMachine;
    }

}///:~