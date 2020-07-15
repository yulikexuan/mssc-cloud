//: guru.sfg.mssc.beer.service.event.BeerEvent.java


package guru.sfg.mssc.beer.service.event;


import guru.sfg.mssc.beer.service.web.model.BeerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder @AllArgsConstructor
public class BeerEvent implements Serializable {

    static final long serialVersionUID = -41648137439928468L;

    private final BeerDto beerDto;

}///:~