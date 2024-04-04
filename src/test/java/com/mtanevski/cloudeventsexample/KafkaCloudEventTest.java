package com.mtanevski.cloudeventsexample;

import static org.assertj.core.api.Assertions.assertThat;

import com.mtanevski.cloudeventsexample.kafka.KafkaCloudEventConsumer;
import com.mtanevski.cloudeventsexample.kafka.KafkaCloudEventProducer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@Slf4j
class KafkaCloudEventTest {

  @Container
  private static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
      .withEmbeddedZookeeper();

  @Test
  @SneakyThrows
  void shouldProduceAndConsumeCloudEvents() {
    var producer = new KafkaCloudEventProducer(KAFKA.getBootstrapServers());
    var publishedEvents = producer.publish(10);

    var consumer = new KafkaCloudEventConsumer(KAFKA.getBootstrapServers());
    var consumedEvents = consumer.consume(10);

    assertThat(consumedEvents).isEqualTo(publishedEvents);
    producer.close();
    consumer.close();
  }

}
