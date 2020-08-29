//: guru.sfg.beer.order.service.web.model.ValidateBeerOrderRequest.java


package guru.sfg.beer.order.service.web.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


@Getter
@AllArgsConstructor(staticName = "of")
public class ValidateBeerOrderRequest implements Serializable {

    static final long serialVersionUID = 6022454365415103803L;

    private final BeerOrderDto beerOrderDto;

}///:~