//: guru.sfg.beer.inventory.service.domain.service.mappers.BeerInventoryMapper.java


package guru.sfg.beer.inventory.service.domain.service.mappers;


import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.web.model.BeerInventoryDto;
import org.mapstruct.Mapper;


@Mapper(uses = {DateMapper.class})
public interface BeerInventoryMapper {
    BeerInventory beerInventoryDtoToBeerInventory(BeerInventoryDto beerInventoryDTO);
    BeerInventoryDto beerInventoryToBeerInventoryDto(BeerInventory beerInventory);
}///:~