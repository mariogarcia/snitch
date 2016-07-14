package columbus.common;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class DefaultProducerManager implements ProducerManager {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * {@inheritDoc}
     */
    public Producer startProducer(Producer producer) {
        executorService.execute(producer);

        return producer;
    }

    /**
     * {@inheritDoc}
     */
    public Producer stopProducer(Producer producer) {
        producer.interrupt();

        return producer;
    }

    public void stop() {
        try {
            System.out.println("Waiting tasks to end before shutting down manager...");
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Shutting down manager NOW!");
                executorService.shutdown();
            }
        } catch(InterruptedException iex) {
            executorService.shutdown();
        }
    }
}
