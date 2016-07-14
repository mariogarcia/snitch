package columbus.kafka;

import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.InputStream;

import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import columbus.common.Producer;

/**
 * Abstraction to produce data and store it to Kafka
 *
 * @since 0.1.0
 */
public abstract class KProducer<T,S> implements Producer<KafkaProducer<T,S>> {

    private final String propertiesFileName;
    private String id;
    protected AtomicBoolean interrupted = new AtomicBoolean(false);

    /**
     * All Kafka producers needs configuration set through
     * a {@link Properties} object. The loading of the properties
     * file is automated once we know the
     */
    public KProducer(final String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void interrupt() {
        interrupted.set(true);
    }

    @Override
    public void run() {
        Properties properties = loadProperties();
        KafkaProducer<T, S> producer = createKafkaProducer(properties);

        try {
            if (producer == null) {
                System.out.println("No kafka producer present...skipping producer execution");
                return;
            }

            execute(producer, properties);
        } catch(Throwable ex) {
            System.out.println(ex.getMessage());
        } finally {
            Optional.ofNullable(producer).ifPresent(KafkaProducer::close);
        }
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream props = Resources.getResource(propertiesFileName).openStream()) {
            properties.load(props);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return properties;
    }

    /**
     *
     * @return
     * @since 0.1.0
     */
    private KafkaProducer<T, S> createKafkaProducer(Properties properties) {
        KafkaProducer<T, S> kafkaProducer = new KafkaProducer<>(properties);

        return kafkaProducer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
