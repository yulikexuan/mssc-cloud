//: guru.sfg.mssc.beer.service.commons.ExecutorServiceFactory.java


package guru.sfg.beer.order.service.commons;


import org.springframework.stereotype.Component;


@Component
public class ExecutorServiceFactory implements IExecutorServiceFactory {

    private ExecutorServiceFactory() {}

    public static ExecutorServiceFactory create() {
        return new ExecutorServiceFactory();
    }

}///:~