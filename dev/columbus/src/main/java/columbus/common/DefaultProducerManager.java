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
    public String startProducer(Producer producer) {
        String uuid = UUID.randomUUID().toString();
        System.out.println("Starting producer: " + uuid);

        producers.put(uuid, producer);
        executorService.execute(producer);

        return uuid;
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
