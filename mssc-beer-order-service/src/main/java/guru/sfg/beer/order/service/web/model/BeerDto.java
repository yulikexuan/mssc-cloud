//: guru.sfg.beer.order.service.web.model.BeerDto.java


package guru.sfg.beer.order.service.web.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@Builder @AllArgsConstructor
public class BeerDto {

    private UUID id = null;

    private Integer version = null;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime lastModifiedDate;

    private String beerName;
    private String beerStyle;
    private String upc;
    private Integer quantityOnHand;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

}///:~