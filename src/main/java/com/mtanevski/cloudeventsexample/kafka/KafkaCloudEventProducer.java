package com.mtanevski.cloudeventsexample.kafka;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.message.Encoding;
import io.cloudevents.jackson.JsonFormat;
import io.cloudevents.kafka.CloudEventSerializer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

@Slf4j
public class KafkaCloudEventProducer {

  private final String bootstrapServersConfig;
  public static final String TOPIC = "sample-topic";
  private Producer<String, CloudEvent> producer;

  public KafkaCloudEventProducer(String bootstrapServersConfig) {
    this.bootstrapServersConfig = bootstrapServersConfig;
  }

  public List<CloudEvent> publish(int messageCount) {
    // Create the KafkaProducer
    producer = buildKafkaProducer();

    // Create an event template to set basic CloudEvent attributes
    return IntStream.range(0, messageCount)
        .mapToObj(KafkaCloudEventProducer::buildCloudEvent)
        .peek(event -> sendRecord(producer, event))
        .toList();
  }

  public void close() {
    producer.flush();
    producer.close();
    producer = null;
  }

  private static CloudEvent buildCloudEvent(int i) {
    String id = UUID.randomUUID().toString();
    String data = "Event number " + i;

    // Create the event starting from the template
    return CloudEventBuilder.v1()
        .withSource(URI.create("localhost"))
        .withType("producer.example")
        .withId(id)
        .withData("text/plain", data.getBytes(StandardCharsets.UTF_8))
        .build();
  }

  private static void sendRecord(Producer<String, CloudEvent> producer, CloudEvent event) {
    // Send the record
    try {
      var metadata = producer
          .send(new ProducerRecord<>(TOPIC, event.getId(), event))
          .get();
      log.info("Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private KafkaProducer<String, CloudEvent> buildKafkaProducer() {
    // Basic producer configuration
    var properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
    properties.put(ProducerConfig.CLIENT_ID_CONFIG, "sample-cloudevents-producer");
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    // Configure the CloudEventSerializer to emit events as json structured events
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CloudEventSerializer.class);
    properties.put(CloudEventSerializer.ENCODING_CONFIG, Encoding.STRUCTURED);
    properties.put(CloudEventSerializer.EVENT_FORMAT_CONFIG, JsonFormat.CONTENT_TYPE);
    return new KafkaProducer<>(properties);
  }
}
