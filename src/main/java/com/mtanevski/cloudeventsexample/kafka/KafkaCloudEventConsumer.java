package com.mtanevski.cloudeventsexample.kafka;

import static com.mtanevski.cloudeventsexample.kafka.KafkaCloudEventProducer.TOPIC;

import io.cloudevents.CloudEvent;
import io.cloudevents.kafka.CloudEventDeserializer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

@Slf4j
public class KafkaCloudEventConsumer {

  private final String bootstrapServersConfig;
  private KafkaConsumer<String, CloudEvent> consumer;

  public KafkaCloudEventConsumer(String bootstrapServersConfig) {
    this.bootstrapServersConfig = bootstrapServersConfig;
  }

  public List<CloudEvent> consume(int messageCount) {
    // Basic consumer configuration
    var properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-cloudevents-consumer");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CloudEventDeserializer.class);
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

    // Create the consumer and subscribe to the topic
    List<CloudEvent> cloudEvents = new ArrayList<>();
    consumer = new KafkaConsumer<>(properties);

    consumer.subscribe(Collections.singletonList(TOPIC));

    for (int i = 0; i < messageCount; i++) {
      ConsumerRecords<String, CloudEvent> consumerRecords = consumer.poll(Duration.ofSeconds(1));
      consumerRecords.forEach(kafkaRecord -> cloudEvents.add(kafkaRecord.value()));
      consumerRecords.forEach(record -> log.info(
          "Record consumed:\n" +
              "  Record Key " + record.key() + "\n" +
              "  Record value " + record.value() + "\n" +
              "  Record partition " + record.partition() + "\n" +
              "  Record offset " + record.offset() + "\n"
      ));
    }
    return cloudEvents;
  }

  public void close() {
    consumer.close();
    consumer = null;
  }
}
