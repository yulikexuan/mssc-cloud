//: guru.sfg.brewery.model.DeallocateBeerOrderRequest.java


package guru.sfg.brewery.model;


import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@Builder @AllArgsConstructor(staticName = "of")
public class DeallocateBeerOrderRequest implements Serializable {

    static final long serialVersionUID = 7481440013657938657L;

    private BeerOrderDto beerOrderDto;

}///:~