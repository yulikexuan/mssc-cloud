//: guru.sfg.mssc.commons.event.NewBeerInventoryEvent.java


package guru.sfg.mssc.commons.event;


import guru.sfg.mssc.commons.dto.BeerDto;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class NewBeerInventoryEvent extends BeerEvent {

    public NewBeerInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }

}///:~