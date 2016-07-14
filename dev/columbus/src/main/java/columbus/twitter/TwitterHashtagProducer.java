package columbus.twitter;

import java.util.List;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import columbus.kafka.KProducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterHashtagProducer extends KProducer<String,String> {

    private final List<String> hashtagList;
    private final String kafkaTopic;

    /**
     *
     * @param hashtagList
     * @param kafkaTopic
     */
    public TwitterHashtagProducer(String kafkaTopic, String... hashtagList) {
        super("twitter.props");

        this.kafkaTopic = kafkaTopic;
        this.hashtagList = Arrays.asList(hashtagList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final KafkaProducer<String, String> producer, Properties properties) {
        String consumerKey = properties.getProperty("api-key");
        String consumerSecret = properties.getProperty("api-secret");
        String token = properties.getProperty("token");
        String secret = properties.getProperty("token-secret");

		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

		endpoint.trackTerms(hashtagList);

		Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);

		// Create a new BasicClient. By default gzip is enabled.
		Client client = new ClientBuilder().hosts(Constants.STREAM_HOST)
				.endpoint(endpoint).authentication(auth)
				.processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();

		// Do whatever needs to be done with messages
		for (int msgRead = 0; msgRead < 1000; msgRead++) {
            System.out.println("Checking if I have to stop");
            if (interrupted.get()) {
                System.out.println("Produced will stop NOW!!!");
                return;
            } else {
                System.out.println("Keep going!!!");
            }

			ProducerRecord<String, String> message = null;
			try {
                System.out.println("Creating message!!!");
				message = new ProducerRecord<String, String>(kafkaTopic, queue.take());
                System.out.println("Sleeping a while!!!");
                Thread.sleep(1000);
			} catch (InterruptedException e) {
                System.out.println("Upppss!!!");
				e.printStackTrace();
			}
            System.out.println("Sending message to kafka");
			producer.send(message);

		}
		client.stop();
    }
}
