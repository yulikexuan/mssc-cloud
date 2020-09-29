//: guru.sfg.brewery.model.AllocateBeerOrderResponse.java


package guru.sfg.brewery.model;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@Getter
@Builder @RequiredArgsConstructor
public class AllocateBeerOrderResponse {

    private final BeerOrderDto beerOrderDto;
    private final Boolean allocationError;
    private final Boolean pendingInventory;

    public UUID getBeerOrderId() {
        return this.beerOrderDto.getId();
    }

}///:~