//: guru.sfg.brewery.model.AllocationFailureTransactionRequest.java


package guru.sfg.brewery.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Getter
@NoArgsConstructor
@Builder @AllArgsConstructor(staticName = "of")
public class AllocationFailureTransactionRequest implements Serializable {

    static final long serialVersionUID = 7059832917308087708L;

    private UUID beerOrderId;

}///:~