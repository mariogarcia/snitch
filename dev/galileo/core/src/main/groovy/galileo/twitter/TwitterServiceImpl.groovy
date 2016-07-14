package galileo.twitter

import rx.Observable
import org.apache.kafka.clients.consumer.*

import com.google.common.io.Resources
import groovy.transform.CompileDynamic
import groovy.json.JsonSlurper

/**
 * Default implementation of the {@link HelloService} interface
 *
 * @since 0.1.0
 */
class TwitterServiceImpl implements TwitterService {

  /**
   * {@inheritDoc}
   */
  @Override
  Map getNextTweet(String topic) {
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(Arrays.asList(topic));

    ConsumerRecords<String,String> records = consumer.poll(1000)
    List<Map> data = records?.collect(this.&toMap) as List<Map>
    String id = data.isEmpty() ? -1 : data?.last()?.id

    consumer.close()

    return [data:data, id: id] as Map
  }


  Map<String,?> toMap(ConsumerRecord<String,String> record) {
    JsonSlurper parser = new JsonSlurper()

    return parser.parseText(record.value()) as Map
  }

  //    @CompileDynamic
  Properties getProperties() {
    return Resources
    .getResource("twitter.properties")
    .openStream()
    .withStream(this.&toProperties) as Properties
  }

  Properties toProperties(InputStream is) {
    Properties properties = new Properties()
    properties.load(is)

    return properties
  }
}
