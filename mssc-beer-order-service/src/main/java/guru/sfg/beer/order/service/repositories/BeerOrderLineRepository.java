package guru.sfg.beer.order.service.repositories;


import guru.sfg.beer.order.service.domain.BeerOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, UUID> {
}
