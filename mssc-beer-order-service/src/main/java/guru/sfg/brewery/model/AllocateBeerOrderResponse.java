//: guru.sfg.brewery.model.AllocateBeerOrderResponse.java


package guru.sfg.brewery.model;


import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@Builder @AllArgsConstructor
public class AllocateBeerOrderResponse {

    private BeerOrderDto beerOrderDto;
    private Boolean allocationError;
    private Boolean pendingInventory;

    public UUID getBeerOrderId() {
        return this.beerOrderDto.getId();
    }

}///:~