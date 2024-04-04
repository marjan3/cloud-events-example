package com.mtanevski.cloudeventsexample.spring.kafka;

import io.cloudevents.CloudEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CloudEventConsumer {

  @KafkaListener(topics = "${kafka.topic}")
  public void receiveCloudEvent(ConsumerRecord<String, CloudEvent> record) {
    CloudEvent cloudEvent = record.value();
    // Process the received CloudEvent
    System.out.println("Received CloudEvent: " + cloudEvent);
  }
}
