//: guru.sfg.beer.order.service.services.NotFoundException.java


package guru.sfg.beer.order.service.services;


public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

}///:~