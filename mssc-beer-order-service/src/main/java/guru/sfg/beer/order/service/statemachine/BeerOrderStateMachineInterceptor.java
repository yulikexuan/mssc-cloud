//: guru.sfg.beer.order.service.statemachine.BeerOrderStateMachineInterceptor.java


package guru.sfg.beer.order.service.statemachine;

import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BeerOrderStateMachineInterceptor extends
        StateMachineInterceptorAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;

    @Override
    @Transactional
    public void preStateChange(
            State<BeerOrderStatusEnum, BeerOrderEventEnum> state,
            Message<BeerOrderEventEnum> message,
            Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition,
            StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine) {

        Optional.ofNullable(message)
                .flatMap(this::getBeerOrderId)
                .flatMap(this.beerOrderRepository::findById)
                .ifPresent(beerOrder -> {
                    log.debug(">>>>>>> Saving state {} for beer order id {} ",
                            state.getId(), beerOrder.getId());
                    beerOrder.setOrderStatus(state.getId());
                    this.beerOrderRepository.saveAndFlush(beerOrder);
                });
    }

    private Optional<UUID> getBeerOrderId(final Message<BeerOrderEventEnum> msg) {
        return Optional.ofNullable((UUID) msg.getHeaders().get(
                BeerOrderManager.BEER_ORDER_ID_HEADER));
    }

}///:~