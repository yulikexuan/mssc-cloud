//: guru.sfg.beer.order.service.services.FailedToPlaceBeerOrderException.java


package guru.sfg.beer.order.service.services;


public class FailedToPlaceBeerOrderException extends RuntimeException {

    public FailedToPlaceBeerOrderException() {
        super();
    }

    public FailedToPlaceBeerOrderException(String msg) {
        super(msg);
    }

}///:~