//: guru.sfg.brewery.model.AllocateBeerOrderRequest.java


package guru.sfg.brewery.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;


@Getter
@RequiredArgsConstructor(staticName = "of")
public class AllocateBeerOrderRequest implements Serializable  {

    static final long serialVersionUID = 280780960648999105L;

    private final BeerOrderDto beerOrderDto;

}///:~