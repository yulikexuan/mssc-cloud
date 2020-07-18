//: guru.sfg.mssc.commons.event.BeerEvent.java


package guru.sfg.mssc.commons.event;


import guru.sfg.mssc.commons.dto.BeerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeerEvent implements Serializable {

    static final long serialVersionUID = -41648137439928468L;

    BeerDto beerDto;

}///:~