package guru.sfg.beer.order.service.web.model;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BeerOrderLineDto extends BaseItem {

    @Builder
    public BeerOrderLineDto(
            UUID id, Integer version,
            OffsetDateTime createdDate, OffsetDateTime lastModifiedDate,
            String upc, String beerName, UUID beerId, String beerStyle,
            Integer orderQuantity, BigDecimal price) {

        super(id, version, createdDate, lastModifiedDate);

        this.upc = upc;
        this.beerName = beerName;
        this.beerId = beerId;
        this.orderQuantity = orderQuantity;
    }

    private String upc;
    private String beerName;
    private UUID beerId;
    private String beerStyle;
    private Integer orderQuantity = 0;
    private BigDecimal price;
}
