//: guru.sfg.brewery.model.AllocateBeerOrderRequest.java


package guru.sfg.brewery.model;


import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@Builder @AllArgsConstructor
public class AllocateBeerOrderRequest implements Serializable  {

    static final long serialVersionUID = 280780960648999105L;

    private BeerOrderDto beerOrderDto;

}///:~