package columbus.kafka;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterProducer {

	private static final String topic = "twitter-topic";

	public static void run(String consumerKey,
                           String consumerSecret,
                           String token,
                           String secret) throws Exception {

        KafkaProducer producer = null;

        try (InputStream props = Resources.getResource("twitter.props").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<>(properties);


            consumerKey = properties.getProperty("api-key");
            consumerSecret = properties.getProperty("api-secret");
            token = properties.getProperty("token");
            secret = properties.getProperty("token-secret");
        }

		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		// add some track terms
		endpoint.trackTerms(Lists.newArrayList("#piweek", "Impuesto de Sociedades", "Sagan", "#vayamovida"));

		Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
		// Authentication auth = new BasicAuth(username, password);

		// Create a new BasicClient. By default gzip is enabled.
		Client client = new ClientBuilder().hosts(Constants.STREAM_HOST)
				.endpoint(endpoint).authentication(auth)
				.processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();

		// Do whatever needs to be done with messages
		for (int msgRead = 0; msgRead < 1000; msgRead++) {
			ProducerRecord<String, String> message = null;
			try {
				message = new ProducerRecord<String, String>(topic, queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			producer.send(message);
		}
		producer.close();
		client.stop();

	}

	public static void main(String[] args) {
		try {
            //			TwitterKafkaProducer.run(args[0], args[1], args[2], args[3]);
			TwitterProducer.run("","","","");
		} catch (Throwable e) {
			System.out.println(e);
		}
	}
}
