package guru.sfg.beer.order.service.domain;


public enum BeerOrderStatusEnum {

    NEW,

    // Validation
    VALIDATED,
    VALIDATION_PENDING,
    VALIDATION_EXCEPTION,

    // Allocation
    ALLOCATED,
    ALLOCATION_PENDING,
    ALLOCATION_EXCEPTION,

    CANCELLED,
    PENDING_INVENTORY,
    PICKED_UP,

    // Delivery
    DELIVERED,
    DELIVERY_EXCEPTION
}