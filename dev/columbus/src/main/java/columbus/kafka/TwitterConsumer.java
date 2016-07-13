package columbus.kafka;

import org.apache.kafka.clients.consumer.*;

import java.io.*;
import java.util.*;
import com.google.common.io.Resources;

public class TwitterConsumer {

  public void consume() throws Exception {
      KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getProperties());
    consumer.subscribe(Arrays.asList("twitter-topic"));

    System.out.println("assignment: " + consumer.assignment());

    while (true) {
        ConsumerRecords<String, String> records = consumer.poll(100);
        System.out.println("records: " + records.count());
        System.out.println("partitions: " + records.partitions());

        for (ConsumerRecord<String, String> record : records)
            System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
    }
  }

  public Properties getProperties() throws Exception {
      InputStream stream = Resources
          .getResource("kafka.properties")
          .openStream();

      Properties properties = new Properties();
      properties.load(stream);

      return properties;
  }

  public static void main(String[] args) throws Exception {
      new TwitterConsumer().consume();
  }
}
