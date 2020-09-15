//: guru.sfg.beer.inventory.service.domain.service.IBeerAllocationService.java


package guru.sfg.beer.inventory.service.domain.service;


import guru.sfg.brewery.model.BeerOrderDto;


public interface IBeerAllocationService {

    boolean allocateBeerOrder(BeerOrderDto beerOrderDto);

}///:~