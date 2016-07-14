package columbus.common;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class DefaultProducerManager implements ProducerManager {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final ConcurrentHashMap<String,Producer> producers = new ConcurrentHashMap<>();


    /**
     * {@inheritDoc}
     */
    public Producer startProducer(Producer producer) {
        executorService.execute(producer);
        String uuid = UUID.randomUUID().toString();
        System.out.println("Starting producer: " + uuid);

        producers.put(uuid, producer);

        return producer;
    }

    /**
     * {@inheritDoc}
     */
    public Producer stopProducer(Producer producer) {
        return producers.computeIfPresent(producer.getId(),(String k, Producer p) -> {
                p.interrupt();
                    return p;
        });
    }

    public Producer stopProducerById(String id) {
        return producers.computeIfPresent(id,(String k, Producer p) -> {
                p.interrupt();
                System.out.println("Stopping producer: " + k);
                return p;
        });
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
