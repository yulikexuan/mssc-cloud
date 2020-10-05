package guru.sfg.beer.order.service.services;


import guru.sfg.beer.order.service.web.model.BeerOrderPagedList;
import guru.sfg.brewery.model.BeerOrderDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;


public interface BeerOrderService {

    BeerOrderPagedList listOrders(UUID customerId, Pageable pageable);

    BeerOrderDto getOrderById(UUID customerId, UUID orderId);

    Optional<UUID> placeOrder(UUID customerId, BeerOrderDto beerOrderDto);

    void pickupOrder(UUID customerId, UUID orderId);
}
