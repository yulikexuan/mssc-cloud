//: guru.sfg.beer.inventory.service.domain.service.IBeerInventoryService.java


package guru.sfg.beer.inventory.service.domain.service;


import guru.sfg.beer.inventory.service.web.model.BeerInventoryDto;
import guru.sfg.mssc.commons.dto.BeerDto;

import java.util.List;
import java.util.UUID;


public interface IBeerInventoryService {

    List<BeerInventoryDto> getAllInventoriesByBeerId(UUID uuid);

    void saveNewInventory(BeerDto beerDto);

}///:~