//: guru.sfg.mssc.beer.service.domain.model.Beer.java


package guru.sfg.mssc.beer.service.domain.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@Builder @AllArgsConstructor
@Entity
public class Beer {

    static final long serialVersionUID = -5815566940065181210L;

    /*
     * By default, Hibernate uses a random number based generation strategy
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar", updatable = false,
            nullable = false)
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    private String beerName;
    private String beerStyle;

    @Column(unique = true)
    private String upc;

    private BigDecimal price;

    private Integer minOnHand;

    private Integer quantityToBrew;

}///:~