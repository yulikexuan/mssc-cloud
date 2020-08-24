//: guru.sfg.beer.order.service.domain.BeerOrderEventEnum.java

package guru.sfg.beer.order.service.domain;


public enum BeerOrderEventEnum {

    VALIDATE_ORDER_EVENT,
    CANCEL_ORDER_EVENT,
    VALIDATION_PASSED_EVENT,
    VALIDATION_FAILED_EVENT,
    ALLOCATE_ORDER_EVENT,
    ALLOCATION_SUCCESS_EVENT,
    ALLOCATION_NO_INVENTORY_EVENT,
    ALLOCATION_FAILED_EVENT,
    BEERORDER_PICKED_UP_EVENT

}///:~