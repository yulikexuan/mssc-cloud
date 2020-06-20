//: guru.sfg.mssc.beer.service.commons.IExecutorServiceFactory.java


package guru.sfg.beer.order.service.commons;


import com.google.common.util.concurrent.MoreExecutors;

import java.time.Duration;
import java.util.concurrent.*;


public interface IExecutorServiceFactory {

    default ScheduledExecutorService newExitingScheduledExecutorService(
            int corePoolSize, Duration terminationTimeout) {

        return MoreExecutors.getExitingScheduledExecutorService(
                new ScheduledThreadPoolExecutor(corePoolSize), terminationTimeout);
    }

    default ExecutorService newExitingExecutorService(
            int corePoolSize, int maximumPoolSize, long keepAliveMillis,
            long terminationTimeoutMillis) {

        BlockingQueue<Runnable> workingQueue = new LinkedTransferQueue<>();

        ThreadPoolExecutor executor =  new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize, keepAliveMillis, TimeUnit.MILLISECONDS, workingQueue);
        executor.allowCoreThreadTimeOut(true);

        return MoreExecutors.getExitingExecutorService(
                executor, Duration.ofMillis(terminationTimeoutMillis));
    }

    default void terminateExecutorService(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            executorService.shutdownNow();
        }
    }

}///:~