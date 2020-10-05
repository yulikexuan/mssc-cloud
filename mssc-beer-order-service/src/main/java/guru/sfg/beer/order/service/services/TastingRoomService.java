package guru.sfg.beer.order.service.services;


import guru.sfg.beer.order.service.bootstrap.BeerOrderBootStrap;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Service
@Slf4j
public class TastingRoomService {

    static final String[] BEER_UPC_REPO = {
            BeerOrderBootStrap.BEER_1_UPC,
            BeerOrderBootStrap.BEER_2_UPC,
            BeerOrderBootStrap.BEER_3_UPC,
            BeerOrderBootStrap.BEER_4_UPC,
            BeerOrderBootStrap.BEER_5_UPC,
            BeerOrderBootStrap.BEER_6_UPC,
            BeerOrderBootStrap.BEER_7_UPC,
    };

    private final CustomerRepository customerRepository;
    private final BeerOrderService beerOrderService;
    private final BeerOrderRepository beerOrderRepository;

    public TastingRoomService(CustomerRepository customerRepository,
                              BeerOrderService beerOrderService,
                              BeerOrderRepository beerOrderRepository) {

        this.customerRepository = customerRepository;
        this.beerOrderService = beerOrderService;
        this.beerOrderRepository = beerOrderRepository;
    }

    // Runs every 30 Munutes
    @Transactional
    @Scheduled(initialDelay = 30000L, fixedDelay = 60000 * 60)
    public void placeTastingRoomOrder() {

        List<Customer> customerList =
                customerRepository.findAllByCustomerNameLike(
                        BeerOrderBootStrap.TASTING_ROOM);

        if (customerList.size() == 1) { //should be just one
            doPlaceOrder(customerList.get(0));
        } else {
            log.error("Too many or too few tasting room customers found");
        }
    }

    private void doPlaceOrder(Customer customer) {

        String beerToOrder = getRandomBeerUpc();

        BeerOrderLineDto beerOrderLine = BeerOrderLineDto.builder()
                .upc(beerToOrder)
                .orderQuantity(new Random().nextInt(6)) // TODO externalize value to property
                .build();

        List<BeerOrderLineDto> beerOrderLineSet = new ArrayList<>();
        beerOrderLineSet.add(beerOrderLine);

        BeerOrderDto beerOrder = BeerOrderDto.builder()
                .customerId(customer.getId())
                .customerRef(UUID.randomUUID().toString())
                .beerOrderLines(beerOrderLineSet)
                .build();

        beerOrderService.placeOrder(customer.getId(), beerOrder);
    }

    private String getRandomBeerUpc() {
        return BEER_UPC_REPO[RandomUtils.nextInt(0, BEER_UPC_REPO.length)];
    }

}
