//: guru.sfg.beer.order.service.services.BeerOrderManager.java


package guru.sfg.beer.order.service.services;


import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class BeerOrderManager implements IBeerOrderManager {

    public static final String BEER_ORDER_ID_HEADER = "beer_order_id";

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum>
            stateMachineFactory;

    private final BeerOrderRepository beerOrderRepository;

    private final StateMachineInterceptor<BeerOrderStatusEnum, BeerOrderEventEnum>
            stateMachineInterceptor;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BeerOrder newBeerOrder(@NonNull BeerOrder beerOrder) {

        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);

        BeerOrder newBeerOrder = this.beerOrderRepository.save(beerOrder);

        this.sendBeerOrderEvent(newBeerOrder, BeerOrderEventEnum.VALIDATE_ORDER_EVENT);

        return newBeerOrder;
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