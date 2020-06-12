//: guru.sfg.mssc.beer.service.web.mapper.BeerMapperDecorator.java


package guru.sfg.mssc.beer.service.web.mapper;


import guru.sfg.mssc.beer.service.domain.model.Beer;
import guru.sfg.mssc.beer.service.domain.services.inventory.IBeerInventoryService;
import guru.sfg.mssc.beer.service.web.model.BeerDto;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class BeerMapperDecorator implements IBeerMapper {

    private IBeerInventoryService beerInventoryService;
    private IBeerMapper mapper;

    @Autowired
    public void setBeerInventoryService(IBeerInventoryService beerInventoryService) {
        this.beerInventoryService = beerInventoryService;
    }

    @Autowired
    public void setMapper(IBeerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Beer beerDtoToBeer(BeerDto beerDto) {
        return mapper.beerDtoToBeer(beerDto);
    }

    @Override
    public BeerDto beerToBeerDto(Beer beer) {
        return mapper.beerToBeerDto(beer);
    }

    @Override
    public BeerDto beerToBeerDtoWithInventory(Beer beer) {
        BeerDto dto = mapper.beerToBeerDto(beer);
        dto.setQuantityOnHand(beerInventoryService.getOnhandInventory(beer.getId()));
        return dto;
    }

}///:~