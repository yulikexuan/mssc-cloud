//: guru.sfg.mssc.commons.event.NewBeerInventoryEvent.java


package guru.sfg.mssc.commons.event;


import guru.sfg.mssc.beer.service.web.model.BeerDto;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class NewBeerInventoryEvent extends BeerEvent {

    public NewBeerInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }

}///:~