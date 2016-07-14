package columbus.common;

import java.util.Properties;

public interface Producer<T> extends Runnable {

    /**
     * This method should set some condition used
     * to interrup this producer somehow
     *
     * @since 0.1.0
     */
    public void interrupt();

    /**
     * @param producer
     * @param properties
     * @since 0.1.0
     */
    public void execute(T producer, Properties properties);
}
