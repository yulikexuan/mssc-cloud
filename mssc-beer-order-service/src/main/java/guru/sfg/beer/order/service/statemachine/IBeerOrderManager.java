//: guru.sfg.beer.order.service.statemachine.IBeerOrderManager.java


package guru.sfg.beer.order.service.statemachine;


import guru.sfg.beer.order.service.domain.BeerOrder;


public interface IBeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);

}///:~