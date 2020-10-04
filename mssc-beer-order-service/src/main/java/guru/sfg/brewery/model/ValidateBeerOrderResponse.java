//: guru.sfg.brewery.model.ValidateBeerOrderResponse.java


package guru.sfg.brewery.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ValidateBeerOrderResponse implements Serializable {

    static final long serialVersionUID = 6022454365415103803L;

    private UUID orderId;
    private List<String> unexistingUpcs;

    public boolean isValid() {
        return Objects.isNull(this.unexistingUpcs) ||
                this.unexistingUpcs.isEmpty();
    }

}///:~