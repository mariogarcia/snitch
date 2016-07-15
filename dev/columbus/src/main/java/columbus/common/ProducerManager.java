package columbus.common;

public interface ProducerManager {

    /**
     * @param producer
     * @return
     * @since 0.1.0
     */
    public String startProducer(Producer producer);

    public Producer stopProducerById(String id);

    /**
     * @since 0.1.0
     */
    public void stop();
}
