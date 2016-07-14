package columbus.common;

public interface ProducerManager {

    /**
     * @param producer
     * @return
     */
    public Producer startProducer(Producer producer);

    /**
     * @param producer
     * @return
     */
    public Producer stopProducer(Producer producer);

    public void stop();
}
