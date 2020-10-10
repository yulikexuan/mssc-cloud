//: guru.sfg.beer.inventory.service.domain.service.BeerAllocationService.java


package guru.sfg.beer.inventory.service.domain.service;


import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.domain.repositories.IBeerInventoryRepository;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.LongAdder;


@Slf4j
@Service
@RequiredArgsConstructor
public class BeerAllocationService implements IBeerAllocationService {

    private final IBeerInventoryRepository beerInventoryRepository;

    @Override
    public boolean allocateBeerOrder(BeerOrderDto beerOrderDto) {

        log.debug(">>>>>>> Allocating Beer Order.  Id: '{}'", beerOrderDto.getId());

        LongAdder totalOrdered = new LongAdder();
        LongAdder totalAllocated = new LongAdder();

        beerOrderDto.getBeerOrderLines().stream()
                .filter(this::isQualifiedForAllocation)
                .forEach(lineDto -> this.allocateBeerOrderLine(
                        lineDto, totalOrdered, totalAllocated));

        log.debug(">>>>>>> Total Ordered: {}", totalOrdered.sum());
        log.debug(">>>>>>> Total Allocated: {}", totalAllocated.sum());

        return totalOrdered.longValue() == totalAllocated.longValue();
    }

    @Override
    public void deallocateBeerOrder(@NonNull BeerOrderDto beerOrderDto) {
        beerOrderDto.getBeerOrderLines().stream()
                .map(orderLine -> BeerInventory.builder()
                        .beerId(orderLine.getBeerId())
                        .upc(orderLine.getUpc())
                        .quantityOnHand(orderLine.getQuantityAllocated())
                        .build())
                .forEach(inventory -> {
                    this.beerInventoryRepository.save(inventory);
                    log.debug(">>>>>>> Deallocated beer order upc - {}",
                            inventory.getUpc());
                });
    }

    private boolean isQualifiedForAllocation(
            @NonNull BeerOrderLineDto beerOrderLineDto) {

        return (((Objects.nonNull(beerOrderLineDto.getOrderQuantity()) ?
                beerOrderLineDto.getOrderQuantity() : 0) -
                (Objects.nonNull(beerOrderLineDto.getQuantityAllocated()) ?
                        beerOrderLineDto.getQuantityAllocated() : 0)) > 0);
    }

    private void allocateBeerOrderLine(
            @NonNull BeerOrderLineDto beerOrderLineDto,
            @NonNull LongAdder totalOrdered,
            @NonNull LongAdder totalAllocated) {

        List<BeerInventory> beerInventoryList = this.beerInventoryRepository
                .findAllByUpc(beerOrderLineDto.getUpc());

        this.beerInventoryRepository.findAllByUpc(beerOrderLineDto.getUpc()).stream()
                .forEach(beerInventory -> this.allocateBeerOrderLineFromBeerInventory(
                        beerInventory, beerOrderLineDto));

        totalOrdered.add(beerOrderLineDto.getOrderQuantity());
        totalAllocated.add((Objects.nonNull(beerOrderLineDto.getQuantityAllocated()) ?
                beerOrderLineDto.getQuantityAllocated() : 0));
    }

    private void allocateBeerOrderLineFromBeerInventory(
            @NonNull BeerInventory beerInventory,
            @NonNull BeerOrderLineDto beerOrderLineDto) {

        int inventoryQuantity = (beerInventory.getQuantityOnHand() == null) ?
                0 : beerInventory.getQuantityOnHand();

        int orderQty = (beerOrderLineDto.getOrderQuantity() == null) ?
                0 : beerOrderLineDto.getOrderQuantity();

        int allocatedQty = (beerOrderLineDto.getQuantityAllocated() == null) ?
                0 : beerOrderLineDto.getQuantityAllocated();

        int qtyToAllocate = orderQty - allocatedQty;

        if (inventoryQuantity >= qtyToAllocate) { // full allocation

            beerOrderLineDto.setQuantityAllocated(orderQty);

            inventoryQuantity = inventoryQuantity - qtyToAllocate;
            beerInventory.setQuantityOnHand(inventoryQuantity);

            this.beerInventoryRepository.save(beerInventory);

        } else if (inventoryQuantity > 0) { //partial allocation
            beerOrderLineDto.setQuantityAllocated(allocatedQty + inventoryQuantity);
            beerInventory.setQuantityOnHand(0);
        }

        if (beerInventory.getQuantityOnHand() == 0) {
            this.beerInventoryRepository.delete(beerInventory);
        }

    }

}///:~