package guru.sfg.beer.order.service.services;


import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.statemachine.IBeerOrderManager;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.beer.order.service.web.model.BeerOrderPagedList;
import guru.sfg.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.NEW;


@Slf4j
@Service
@AllArgsConstructor
public class BeerOrderServiceImpl implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final IBeerOrderManager beerOrderManager;

    @Override
    public BeerOrderPagedList listOrders(UUID customerId, Pageable pageable) {

        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {

            Page<BeerOrder> beerOrderPage = beerOrderRepository.findAllByCustomer(
                    customerOptional.get(), pageable);

            return new BeerOrderPagedList(beerOrderPage
                    .stream()
                    .map(beerOrderMapper::beerOrderToDto)
                    .collect(Collectors.toList()), PageRequest.of(
                    beerOrderPage.getPageable().getPageNumber(),
                    beerOrderPage.getPageable().getPageSize()),
                    beerOrderPage.getTotalElements());

        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public BeerOrderDto placeOrder(final UUID customerId,
                                   final BeerOrderDto beerOrderDto) {

        Customer customer = this.customerRepository.findById(customerId).orElseThrow(
                () -> {
                    String errMsg = String.format(">>>>>>> Customer not found: {}",
                            customerId);
                    log.error(errMsg);
                    return new NotFoundException(errMsg);
                }
        );

        UUID beerOrderId = this.placeOrderForCustomer(customer, beerOrderDto)
                .orElseThrow(() -> {
                    String errMsg = String.format(
                            ">>>>>>> Failed to place BeerOrder for {}",
                            customer.getCustomerName());
                    log.error(errMsg);
                    return new FailedToPlaceBeerOrderException(errMsg);
                });

        // TODO: Cycling test the BeerOrder status to see the final status

        BeerOrder placedBeerOrder = this.beerOrderRepository.findById(beerOrderId)
                .orElseThrow(() -> {
                    String errMsg = String.format(">>>>>>> BeerOrder not found: {}",
                            beerOrderId.toString());
                    log.error(errMsg);
                    return new NotFoundException(errMsg);
                });

        return this.beerOrderMapper.beerOrderToDto(placedBeerOrder);
    }

    private Optional<UUID> placeOrderForCustomer(
            @NonNull final Customer customer,
            @NonNull final BeerOrderDto beerOrderDto) {

        BeerOrder beerOrder = this.beerOrderMapper.dtoToBeerOrder(beerOrderDto);
        beerOrder.setId(null);
        beerOrder.setCustomer(customer);
        beerOrder.setOrderStatus(NEW);
        beerOrder.getBeerOrderLines().forEach(line -> line.setBeerOrder(beerOrder));

        return this.beerOrderManager.newBeerOrder(beerOrder);
    }

    @Override
    public BeerOrderDto getOrderById(UUID customerId, UUID orderId) {
        return beerOrderMapper.beerOrderToDto(getOrder(customerId, orderId));
    }

    @Override
    public void pickupOrder(@NonNull final UUID customerId,
                            @NonNull final UUID orderId) {

        this.beerOrderRepository.findById(orderId)
                .ifPresentOrElse(
                        order -> {
                            if (!order.getCustomer().getId().equals(customerId)) {
                                String errMsg = String.format(
                                        ">>>>>>> The BeerOrder %s is not " +
                                                "for Customer %s",
                                        orderId, customerId);
                                throw new IllegalStateException(errMsg);
                            }
                            this.beerOrderManager.beerOrderPickedUp(orderId);
                        },
                        () -> {
                            String errMsg = String.format(
                                    ">>>>>>> BeerOrder not found: {}",
                                    orderId);
                            log.error(errMsg);
                            throw new NotFoundException(errMsg);
                        });
    }

    private BeerOrder getOrder(UUID customerId, UUID orderId) {

        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if(customerOptional.isPresent()){
            Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(orderId);

            if(beerOrderOptional.isPresent()){
                BeerOrder beerOrder = beerOrderOptional.get();

                // fall to exception if customer id's do not match - order not for customer
                if(beerOrder.getCustomer().getId().equals(customerId)){
                    return beerOrder;
                }
            }
            throw new RuntimeException("Beer Order Not Found");
        }

        throw new RuntimeException("Customer Not Found");
    }
}
