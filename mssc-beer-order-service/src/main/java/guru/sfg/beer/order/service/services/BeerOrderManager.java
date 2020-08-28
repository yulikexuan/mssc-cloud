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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class BeerOrderManager implements IBeerOrderManager {

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum>
            stateMachineFactory;

    private final BeerOrderRepository beerOrderRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BeerOrder newBeerOrder(@NonNull BeerOrder beerOrder) {

        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);
        this.sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATE_ORDER_EVENT);
        return this.beerOrderRepository.save(beerOrder);
    }

    private void sendBeerOrderEvent(@NonNull BeerOrder beerOrder,
                                    @NonNull BeerOrderEventEnum event) {

        var stateMachine =
                this.buildStateMachine(beerOrder);

        var msg = MessageBuilder.withPayload(event).build();

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
                .doWithAllRegions(stateMachineAccessor ->
                        stateMachineAccessor.resetStateMachine(stateMachineContext));

        stateMachine.start();

        return stateMachine;
    }

}///:~