package com.mtanevski.cloudeventsexample.spring.kafka;

import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CloudEventProducer {

  @Autowired
  private KafkaTemplate<String, CloudEvent> kafkaTemplate;

  public void sendCloudEvent(CloudEvent cloudEvent, String topic) {
    kafkaTemplate.send(topic, cloudEvent);
  }
}