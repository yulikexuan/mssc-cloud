//: guru.sfg.mssc.beer.service.event.BeerEvent.java


package guru.sfg.mssc.beer.service.event;


import guru.sfg.mssc.beer.service.web.model.BeerDto;
import lombok.Data;

import java.io.Serializable;


@Data
public class BeerEvent implements Serializable {

    static final long serialVersionUID = -41648137439928468L;

    private final BeerDto beerDto;

    BeerEvent(BeerDto beerDto) {
        this.beerDto = beerDto;
    }

    public static BeerEvent of(BeerDto beerDto) {
        return new BeerEvent(beerDto);
    }

}///:~