package guru.sfg.beer.order.service.repositories;


import guru.sfg.beer.order.service.domain.BeerOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, UUID> {
}
