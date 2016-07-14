package columbus.common;

public interface ProducerManager {

    /**
     * @param producer
     * @return
     * @since 0.1.0
     */
    public Producer startProducer(Producer producer);

    /**
     * @param producer
     * @return
     * @since 0.1.0
     */
    public Producer stopProducer(Producer producer);

    public Producer stopProducerById(String id);

    /**
     * @since 0.1.0
     */
    public void stop();
}
