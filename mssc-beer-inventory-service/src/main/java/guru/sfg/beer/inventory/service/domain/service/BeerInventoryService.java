//: guru.sfg.beer.inventory.service.domain.service.BeerInventoryService.java


package guru.sfg.beer.inventory.service.domain.service;


import com.google.common.collect.ImmutableList;
import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.domain.repositories.IBeerInventoryRepository;
import guru.sfg.beer.inventory.service.domain.service.mappers.BeerInventoryMapper;
import guru.sfg.beer.inventory.service.web.model.BeerInventoryDto;
import guru.sfg.mssc.commons.dto.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BeerInventoryService implements IBeerInventoryService {

    private final IBeerInventoryRepository beerInventoryRepository;
    private final BeerInventoryMapper beerInventoryMapper;

    @Override
    public List<BeerInventoryDto> getAllInventoriesByBeerId(UUID beerId) {

        log.debug(">>>>>>> Finding Inventory for beerId: {}", beerId);

        return beerInventoryRepository.findAllByBeerId(beerId)
                .stream()
                .map(beerInventoryMapper::beerInventoryToBeerInventoryDto)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public void saveNewInventory(BeerDto beerDto) {

        BeerInventory beerInventory = BeerInventory.builder()
                .beerId(beerDto.getId())
                .upc(beerDto.getUpc())
                .quantityOnHand(beerDto.getQuantityOnHand())
                .build();

        this.beerInventoryRepository.save(beerInventory);
    }

}///:~