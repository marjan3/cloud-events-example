package com.mtanevski.cloudeventsexample;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpringKafkaCloudEventTest {

  @Container
  private static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
      .withEmbeddedZookeeper();

  @Autowired
  KafkaTemplate<String, CloudEvent> kafkaTemplate;
  @Autowired
  ConsumerFactory<String, CloudEvent> consumerFactory;

  @SneakyThrows
  @Test
  void shouldTest() {
    String topic = "my-topic";

    kafkaTemplate.send(topic, "test", CloudEventBuilder.v1().withId("test")
            .withType("testxs")
        .withSource(URI.create("http://localhost:9092")).build());
    kafkaTemplate.flush();

    // Wait for the Kafka listener to process the message
    Thread.sleep(1000);
  }

  @DynamicPropertySource
  static void dynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);

  }

}
