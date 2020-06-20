//: guru.sfg.beer.order.service.web.mappers.BeerOrderLineMapperDecorator.java


package guru.sfg.beer.order.service.web.mappers;


import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.services.beer.IBeerService;
import guru.sfg.beer.order.service.web.model.BeerOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper {

    private IBeerService beerService;
    private BeerOrderLineMapper beerOrderLineMapper;

    @Autowired
    public void setBeerService(IBeerService beerService) {
        this.beerService = beerService;
    }

    @Autowired
    @Qualifier("delegate")
    public void setBeerOrderLineMapper(BeerOrderLineMapper beerOrderLineMapper) {
        this.beerOrderLineMapper = beerOrderLineMapper;
    }

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {

        final BeerOrderLineDto orderLineDto =
                this.beerOrderLineMapper.beerOrderLineToDto(line);

        beerService.getBeerByUpc(line.getUpc()).thenAccept(
                beerDtoOpt -> beerDtoOpt.ifPresent(beerDto -> {
                    orderLineDto.setBeerName(beerDto.getBeerName());
                    orderLineDto.setBeerStyle(beerDto.getBeerStyle());
                    orderLineDto.setPrice(beerDto.getPrice());
                    orderLineDto.setBeerId(beerDto.getId());
                }));

        return orderLineDto;
    }

}///:~