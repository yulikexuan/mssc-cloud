//: guru.sfg.mssc.beer.service.event.BrewBeerEvent.java


package guru.sfg.mssc.beer.service.event;


import guru.sfg.mssc.beer.service.web.model.BeerDto;


public class BrewBeerEvent extends BeerEvent {

    private BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }

    public static BrewBeerEvent of(BeerDto beerDto) {
        return new BrewBeerEvent(beerDto);
    }

}///:~