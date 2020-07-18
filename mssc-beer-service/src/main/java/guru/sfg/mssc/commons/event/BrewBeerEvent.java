//: guru.sfg.mssc.commons.event.BrewBeerEvent.java


package guru.sfg.mssc.commons.event;


import guru.sfg.mssc.beer.service.web.model.BeerDto;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class BrewBeerEvent extends BeerEvent {

    private BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }

    public static BrewBeerEvent of(BeerDto beerDto) {
        return new BrewBeerEvent(beerDto);
    }

}///:~