// guru.sfg.beer.inventory.service.domain.repositories.IBeerInventoryRepository.java

package guru.sfg.beer.inventory.service.domain.repositories;


import guru.sfg.beer.inventory.service.domain.BeerInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface IBeerInventoryRepository extends JpaRepository<BeerInventory, UUID> {

    /*
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
     */
    List<BeerInventory> findAllByBeerId(UUID beerId);

}
