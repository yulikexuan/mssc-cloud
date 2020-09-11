//: guru.sfg.brewery.model.ValidateBeerOrderRequest.java


package guru.sfg.brewery.model;


import lombok.*;

import java.io.Serializable;


@Getter
@NoArgsConstructor
@Builder @AllArgsConstructor(staticName = "of")
public class ValidateBeerOrderRequest implements Serializable {

    static final long serialVersionUID = 6022454365415103803L;

    private BeerOrderDto beerOrderDto;

}///:~