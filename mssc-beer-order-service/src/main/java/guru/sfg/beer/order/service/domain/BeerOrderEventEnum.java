//: guru.sfg.beer.order.service.domain.BeerOrderEventEnum.java


package guru.sfg.beer.order.service.domain;

public enum BeerOrderEventEnum {

    VALIDATE_ORDER,
    VALIDATION_PASSED,
    VALIDATION_FAILED,

    ALLOCATION_SUCCESS,
    ALLOCATION_NO_INVENTORY,
    ALLOCATION_FAILED,

    BEER_ORDER_PICKED_UP, 

    // Validation
    VALIDATED,
    VALIDATION_PENDING,
    VALIDATION_EXCEPTION,

    // Allocation
    ALLOCATION_PENDING,
    ALLOCATED,
    ALLOCATION_EXCEPTION,

    CANCELLED,
    PENDING_INVENTORY,
    PICKED_UP,

    // Delivery
    DELIVERED,
    DELIVERY_EXCEPTION

}///:~